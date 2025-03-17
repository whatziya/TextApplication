package com.whatziya.textapplication.fragments.signin

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.utilities.Constants

class SignInViewModel(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {
    private val database = FirebaseFirestore.getInstance()

    private val _validationResult = MutableLiveData<Int>()
    val validationResult: LiveData<Int> = _validationResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _pass = MutableLiveData<Boolean>()
    val pass: LiveData<Boolean> = _pass

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun signIn(email: String, password: String) {
        _loading.postValue(true)

        database.collection(Constants.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_EMAIL, email)
            .whereEqualTo(Constants.KEY_PASSWORD, password)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents.orEmpty()
                    if (documents.isNotEmpty()) {
                        val documentSnapshot = documents.firstOrNull()
                        documentSnapshot?.let { doc ->
                            preferenceProvider.apply {
                                isSignedIn = true
                                userId = doc.id
                                name = doc.getString(Constants.KEY_NAME).orEmpty()
                                image = doc.getString(Constants.KEY_IMAGE).orEmpty()
                            }
                            _pass.postValue(true)
                        } ?: _error.postValue("No user data found")
                    } else {
                        _error.postValue("Invalid email or password")
                    }
                } else {
                    _error.postValue(task.exception?.message ?: "Sign-in failed. Try again.")
                }
            }
            .addOnFailureListener {
                _loading.postValue(false)
            }
    }


    fun isValidSignInDetails(email: String, password: String): Int {
        val result = when {
            email.isBlank() -> 0
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> 1
            password.isBlank() -> 2
            else -> -1
        }
        _validationResult.postValue(result)
        return result
    }
}