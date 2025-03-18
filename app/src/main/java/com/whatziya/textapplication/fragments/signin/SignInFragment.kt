package com.whatziya.textapplication.fragments.signin

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.whatziya.textapplication.R
import com.whatziya.textapplication.SharedViewModel
import com.whatziya.textapplication.databinding.FragmentSigninBinding
import com.whatziya.textapplication.events.NavGraphEvent
import com.whatziya.textapplication.extensions.toast
import com.whatziya.textapplication.extensions.visibleIf
import com.whatziya.textapplication.fragments.BaseFragment
import com.whatziya.textapplication.fragments.factory.ViewModelFactory
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.preferences.SharedPreferencesHelper

class SignInFragment : BaseFragment(R.layout.fragment_signin) {
    private val binding by viewBinding(FragmentSigninBinding::bind)
    private val viewModel: SignInViewModel by viewModels {
        ViewModelFactory(
            providePreferences()
        )
    }
    private val sharedVm: SharedViewModel by activityViewModels {
        ViewModelFactory(
            providePreferences()
        )
    }

    override fun setup() {

    }

    override fun clicks() = with(binding){
        textCreateNewAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        buttonSignin.setOnClickListener {
            if (viewModel.isValidSignInDetails(inputEmail.text.toString(), inputPassword.text.toString()) == -1){
                viewModel.signIn(inputEmail.text.toString(), inputPassword.text.toString())
            }
        }
    }

    override fun observe() = with(viewModel) {
        validationResult.observe(viewLifecycleOwner){
            val messages = listOf("Enter email", "Enter valid email", "Enter password")
            if (it in messages.indices) toast(messages[it])
        }

        loading.observe(viewLifecycleOwner){
            with(binding) {
                buttonSignin.visibleIf(!it)
                progressBar.visibleIf(it)
            }
        }

        pass.observe(viewLifecycleOwner){
            if (it){
                sharedVm.setNavGraphEvent(NavGraphEvent.Main)
            }
        }

        error.observe(viewLifecycleOwner){
            toast(it)
        }
    }

    private fun providePreferences(): PreferenceProvider {
        return PreferenceProvider(
            SharedPreferencesHelper.provideSharedPreferences(requireContext())
        )
    }
}