package com.whatziya.textapplication.preferences

import android.content.SharedPreferences

class PreferenceProvider(private val preferences: SharedPreferences) {
    var isSignedIn: Boolean by preferences.boolean()
    var userId: String by preferences.string()
    var name: String by preferences.string()
    var image: String by preferences.string()

    fun clear(){
        preferences.edit().clear().apply()
    }
}