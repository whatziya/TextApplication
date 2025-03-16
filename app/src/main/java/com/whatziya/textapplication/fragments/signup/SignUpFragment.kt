package com.whatziya.textapplication.fragments.signup

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.whatziya.textapplication.R
import com.whatziya.textapplication.SharedViewModel
import com.whatziya.textapplication.databinding.FragmentSignupBinding
import com.whatziya.textapplication.fragments.BaseFragment
import com.whatziya.textapplication.fragments.factory.ViewModelFactory
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.preferences.SharedPreferencesHelper

class SignUpFragment : BaseFragment(R.layout.fragment_signup) {
    private val binding by viewBinding(FragmentSignupBinding::bind)
    private val viewModel: SignUpViewModel by viewModels {
        ViewModelFactory(PreferenceProvider(SharedPreferencesHelper.provideSharedPreferences(requireContext())))
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


    override fun setup(){

    }
}