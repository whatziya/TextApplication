package com.whatziya.textapplication.fragments.users

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.whatziya.textapplication.R
import com.whatziya.textapplication.databinding.FragmentUsersBinding
import com.whatziya.textapplication.fragments.BaseFragment
import com.whatziya.textapplication.fragments.factory.ViewModelFactory
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.preferences.SharedPreferencesHelper

class UsersFragment : BaseFragment(R.layout.fragment_users) {
    private val binding by viewBinding(FragmentUsersBinding::bind)
    private val viewModel: UsersViewModel by viewModels {
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