package com.whatziya.textapplication.fragments.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.whatziya.textapplication.SharedViewModel
import com.whatziya.textapplication.fragments.chat.ChatViewModel
import com.whatziya.textapplication.fragments.main.MainViewModel
import com.whatziya.textapplication.fragments.signin.SignInViewModel
import com.whatziya.textapplication.fragments.signup.SignUpViewModel
import com.whatziya.textapplication.fragments.users.UsersViewModel
import com.whatziya.textapplication.preferences.PreferenceProvider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val preferenceProvider: PreferenceProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(preferenceProvider) as T
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> SignInViewModel(preferenceProvider) as T
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> SignUpViewModel(preferenceProvider) as T
            modelClass.isAssignableFrom(UsersViewModel::class.java) -> UsersViewModel(preferenceProvider) as T
            modelClass.isAssignableFrom(SharedViewModel::class.java) -> SharedViewModel(preferenceProvider) as T
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> ChatViewModel(preferenceProvider) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
