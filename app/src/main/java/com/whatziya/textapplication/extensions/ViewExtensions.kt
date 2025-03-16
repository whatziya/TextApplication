package com.whatziya.textapplication.extensions

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visibleIf(condition: Boolean) {
    if (condition) visible() else gone()
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}