package com.whatziya.textapplication.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val image: String,
    val email: String,
    val token: String,
    val id: String
) : Parcelable
