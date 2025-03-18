package com.whatziya.textapplication.fragments.users

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.whatziya.textapplication.R
import com.whatziya.textapplication.adapters.UsersAdapter
import com.whatziya.textapplication.databinding.FragmentUsersBinding
import com.whatziya.textapplication.extensions.visibleIf
import com.whatziya.textapplication.fragments.BaseFragment
import com.whatziya.textapplication.fragments.factory.ViewModelFactory
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.preferences.SharedPreferencesHelper
import com.whatziya.textapplication.utilities.Constants

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

    private val adapter by lazy {
        UsersAdapter {
            val bundle = Bundle()
            bundle.putParcelable(Constants.KEY_USER, it)
            findNavController().navigate(R.id.action_usersFragment_to_chatFragment, bundle)
        }
    }

    override fun setup() = with(binding) {
        userRecyclerView.adapter = adapter
    }

    override fun clicks() = with(binding) {
        imageBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun observe() = with(viewModel) {
        loading.observe(viewLifecycleOwner) {
            binding.progressBar.visibleIf(it)
        }
        users.observe(viewLifecycleOwner) {
            binding.userRecyclerView.visibleIf(it.isNotEmpty())
            binding.textErrorMessage.visibleIf(it.isEmpty())
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            } else {
                binding.textErrorMessage.text = getString(R.string.no_user_available)
            }
        }
    }
}