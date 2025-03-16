package com.whatziya.textit.preferences

import android.content.Context
import android.content.SharedPreferences
import com.whatziya.textit.utilities.Constants

object SharedPreferencesHelper {
    fun provideSharedPreferences(context : Context) :SharedPreferences{
        return context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }
}