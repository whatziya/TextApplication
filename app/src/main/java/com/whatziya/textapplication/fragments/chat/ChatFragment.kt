package com.whatziya.textapplication.fragments.chat

import android.os.Build
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.whatziya.textapplication.R
import com.whatziya.textapplication.databinding.FragmentChatBinding
import com.whatziya.textapplication.fragments.BaseFragment
import com.whatziya.textapplication.fragments.factory.ViewModelFactory
import com.whatziya.textapplication.models.User
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.preferences.SharedPreferencesHelper
import com.whatziya.textapplication.utilities.Constants

class ChatFragment : BaseFragment(R.layout.fragment_chat) {
    private val binding by viewBinding(FragmentChatBinding::bind)
    private val viewModel: ChatViewModel by viewModels {
        ViewModelFactory(
            PreferenceProvider(
                SharedPreferencesHelper.provideSharedPreferences(
                    requireContext()
                )
            )
        )
    }

    private val user: User? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(Constants.KEY_USER, User::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(Constants.KEY_USER)
        }
    }

    override fun setup() {
        binding.textName.text = user?.name
    }

    override fun clicks() = with(binding) {
        imageBack.setOnClickListener{
            findNavController().popBackStack()
        }
    }
}