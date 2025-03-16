package com.whatziya.textapplication.fragments.signin

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.whatziya.textapplication.preferences.PreferenceProvider

class SignInViewModel(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {
    private val database = FirebaseFirestore.getInstance()
}