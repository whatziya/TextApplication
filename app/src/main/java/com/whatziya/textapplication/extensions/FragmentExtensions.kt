package com.whatziya.textapplication.extensions

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Fragment.toast(@StringRes message: Int) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}