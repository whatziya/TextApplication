package com.whatziya.textapplication.fragments.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.whatziya.textapplication.models.User
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.utilities.Constants

class UsersViewModel(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {
    private val database = FirebaseFirestore.getInstance()

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> = _loading

    private val _users = MutableLiveData<List<User>>()
    val users: MutableLiveData<List<User>> = _users

    init {
        getUsers()
    }

    private fun getUsers() {
        _loading.postValue(true)

        database.collection(Constants.KEY_COLLECTION_USERS)
            .get()
            .addOnSuccessListener { result ->
                _loading.postValue(false)
                val currentUserId = preferenceProvider.userId
                val usersList = result.documents
                    .filterNot { it.id == currentUserId }
                    .map { document ->
                        User(
                            name = document.getString(Constants.KEY_NAME) ?: "",
                            email = document.getString(Constants.KEY_EMAIL) ?: "",
                            image = document.getString(Constants.KEY_IMAGE) ?: "",
                            token = document.getString(Constants.KEY_FCM_TOKEN) ?: "",
                        )
                    }

                _users.value = usersList
            }
            .addOnFailureListener {
                _loading.postValue(false)
                _users.value = emptyList()
            }
    }

}