package com.whatziya.textapplication.fragments.main

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.whatziya.textapplication.R
import com.whatziya.textapplication.SharedViewModel
import com.whatziya.textapplication.databinding.FragmentMainBinding
import com.whatziya.textapplication.events.NavGraphEvent
import com.whatziya.textapplication.extensions.toast
import com.whatziya.textapplication.fragments.BaseFragment
import com.whatziya.textapplication.fragments.factory.ViewModelFactory
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.preferences.SharedPreferencesHelper

class MainFragment : BaseFragment(R.layout.fragment_main) {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels {
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

    override fun setup() {

    }

    override fun clicks() = with(binding) {
        imageSignout.setOnClickListener { viewModel.signOut() }
        fabNewChat.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_usersFragment)
        }
    }

    override fun observe() = with(viewModel) {
        name.observe(viewLifecycleOwner) {
            binding.textName.text = it
        }
        bitmap.observe(viewLifecycleOwner) {
            binding.imageProfile.setImageBitmap(it)
        }
        toast.observe(viewLifecycleOwner) {
            toast(it)
        }
        exit.observe(viewLifecycleOwner) {
            if (it) {
                sharedVm.setNavGraphEvent(NavGraphEvent.Login)
            }
        }
    }
}