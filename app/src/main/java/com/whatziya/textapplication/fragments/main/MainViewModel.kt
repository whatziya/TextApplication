package com.whatziya.textapplication.fragments.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.utilities.Constants

class MainViewModel(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {
    private val database = FirebaseFirestore.getInstance()

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: MutableLiveData<Bitmap> = _bitmap

    private val _name = MutableLiveData<String>()
    val name: MutableLiveData<String> = _name

    private val _toast = MutableLiveData<String>()
    val toast: MutableLiveData<String> = _toast

    private val _exit = MutableLiveData<Boolean>()
    val exit: MutableLiveData<Boolean> = _exit

    init{
        loadUserDetails()
        getToken()
    }

    private fun getToken(){
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { updateToken(it) }
    }

    private fun updateToken(token : String){
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceProvider.userId)
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
            .addOnFailureListener {_toast.postValue("Unable to update token")}
    }

    private fun loadUserDetails() {
        _name.postValue(preferenceProvider.name)
        _bitmap.postValue(
            runCatching {
                val bytes = Base64.decode(preferenceProvider.image, Base64.NO_WRAP)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: getDefaultBitmap()
            }.getOrDefault(getDefaultBitmap())
        )
    }

    private fun getDefaultBitmap(): Bitmap {
        return Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.GRAY)
        }
    }

    fun signOut(){
        _toast.postValue("Signing out...")
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceProvider.userId)
        val updates = hashMapOf<String, Any>(
            Constants.KEY_FCM_TOKEN to FieldValue.delete()
        )
        documentReference.update(updates)
            .addOnSuccessListener {
                preferenceProvider.clear()
                _exit.postValue(true)
                _toast.postValue("Signed out successfully")
            }
            .addOnFailureListener {
                _toast.postValue("Unable to sign out")
            }

    }
}