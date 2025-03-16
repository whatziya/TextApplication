package com.whatziya.textapplication.fragments.signup

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.whatziya.textapplication.preferences.PreferenceProvider

class SignUpViewModel(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {
    private val database = FirebaseFirestore.getInstance()
}