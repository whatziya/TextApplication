package com.whatziya.textapplication.fragments.signup

import android.graphics.Bitmap
import android.util.Base64
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.utilities.Constants
import java.io.ByteArrayOutputStream

class SignUpViewModel(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {
    private val database = FirebaseFirestore.getInstance()

    private val _validationResult = MutableLiveData<Int>()
    val validationResult: LiveData<Int> = _validationResult

    private val _encodedImage = MutableLiveData<String>()
    val encodedImage: LiveData<String> = _encodedImage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _pass = MutableLiveData<Boolean>()
    val pass: LiveData<Boolean> = _pass

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun signUp(inputName: String, inputEmail: String, inputPassword: String){
        _loading.value = true;
        val user = hashMapOf(
            Constants.KEY_NAME to inputName,
            Constants.KEY_EMAIL to inputEmail,
            Constants.KEY_PASSWORD to inputPassword,
            Constants.KEY_IMAGE to _encodedImage.value
        )
        database.collection(Constants.KEY_COLLECTION_USERS)
            .add(user)
            .addOnSuccessListener { documentReference ->
                _loading.value = false
                preferenceProvider.isSignedIn = true
                preferenceProvider.userId = documentReference.id
                preferenceProvider.name = inputName
                preferenceProvider.image = _encodedImage.value.toString()
                _pass.value = true
            }
            .addOnFailureListener {
                _loading.value = false
                _error.value = it.message
            }
    }

    fun encodeImage(bitmap: Bitmap) {
        val previewWidth = 150
        val previewHeight = (bitmap.height * previewWidth) / bitmap.width
        val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)

        ByteArrayOutputStream().use { outputStream ->
            previewBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            val bytes = outputStream.toByteArray()
            _encodedImage.value = Base64.encodeToString(bytes, Base64.NO_WRAP)
        }
    }


    fun isValidSignUpDetails(name: String, email: String, password: String, confirmPassword: String): Int {
        val result = when {
            _encodedImage.value == null -> 0
            name.trim().isEmpty() -> 1
            email.trim().isEmpty() -> 2
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> 3
            password.trim().isEmpty() -> 4
            confirmPassword.trim().isEmpty() -> 5
            password != confirmPassword -> 6
            else -> -1
        }
        _validationResult.value = result
        return result
    }
}