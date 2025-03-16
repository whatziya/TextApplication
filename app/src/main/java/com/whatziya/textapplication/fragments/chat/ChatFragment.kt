package com.whatziya.textapplication.fragments.chat

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.whatziya.textapplication.R
import com.whatziya.textapplication.databinding.FragmentChatBinding
import com.whatziya.textapplication.fragments.BaseFragment
import com.whatziya.textapplication.fragments.factory.ViewModelFactory
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.preferences.SharedPreferencesHelper

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

    override fun setup() {

    }
}