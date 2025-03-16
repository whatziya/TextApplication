package com.whatziya.textapplication.fragments.signup

import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.whatziya.textapplication.R
import com.whatziya.textapplication.SharedViewModel
import com.whatziya.textapplication.databinding.FragmentSignupBinding
import com.whatziya.textapplication.events.NavGraphEvent
import com.whatziya.textapplication.extensions.gone
import com.whatziya.textapplication.extensions.toast
import com.whatziya.textapplication.extensions.visibleIf
import com.whatziya.textapplication.fragments.BaseFragment
import com.whatziya.textapplication.fragments.factory.ViewModelFactory
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.preferences.SharedPreferencesHelper

class SignUpFragment : BaseFragment(R.layout.fragment_signup) {
    private val binding by viewBinding(FragmentSignupBinding::bind)
    private val viewModel: SignUpViewModel by viewModels {
        ViewModelFactory(
            PreferenceProvider(
                SharedPreferencesHelper.provideSharedPreferences(
                    requireContext()
                )
            )
        )
    }
    private val sharedVm: SharedViewModel by activityViewModels {
        ViewModelFactory(
            PreferenceProvider(
                SharedPreferencesHelper.provideSharedPreferences(
                    requireContext()
                )
            )
        )
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val imageUri = result.data?.data ?: return@registerForActivityResult
        try {
            context?.contentResolver?.openInputStream(imageUri)?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream) ?: return@use
                binding.imageProfile.setImageBitmap(bitmap)
                binding.textAddImage.gone()
                viewModel.encodeImage(bitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun setup() {

    }

    override fun clicks() = with(binding) {
        textSignIn.setOnClickListener { findNavController().popBackStack() }
        buttonSignup.setOnClickListener {
            if (viewModel.isValidSignUpDetails(
                    inputName.text.toString(),
                    inputEmail.text.toString(),
                    inputPassword.text.toString(),
                    inputConfirmPassword.text.toString()
                ) == -1
            ) {
                viewModel.signUp(
                    inputName.text.toString(),
                    inputEmail.text.toString(),
                    inputPassword.text.toString()
                )
            }
        }
        layoutImage.setOnClickListener {
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                pickImage.launch(this)
            }
        }
    }

    override fun observe() = with(viewModel) {
        loading.observe(viewLifecycleOwner) {
            with(binding) {
                buttonSignup.visibleIf(!it)
                progressBar.visibleIf(it)
            }
        }

        validationResult.observe(viewLifecycleOwner) {
            validationResult.observe(viewLifecycleOwner) {
                val messages = listOf(
                    "Set profile image", "Enter name", "Enter email", "Enter valid email", "Enter password",
                    "Confirm your password", "Password & confirm password must be same"
                )
                if (it in messages.indices) toast(messages[it])
            }
        }

        error.observe(viewLifecycleOwner){
            toast(it)
        }

        pass.observe(viewLifecycleOwner){
            if (it){
                sharedVm.setNavGraphEvent(NavGraphEvent.Main)
            }
        }
    }
}