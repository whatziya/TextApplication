package com.whatziya.textapplication.preferences

import android.content.Context
import android.content.SharedPreferences
import com.whatziya.textapplication.utilities.Constants

object SharedPreferencesHelper {
    fun provideSharedPreferences(context : Context) :SharedPreferences{
        return context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }
}