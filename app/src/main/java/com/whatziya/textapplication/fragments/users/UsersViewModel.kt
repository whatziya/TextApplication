package com.whatziya.textapplication.fragments.users

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.whatziya.textapplication.preferences.PreferenceProvider

class UsersViewModel(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {
    private val database = FirebaseFirestore.getInstance()

}