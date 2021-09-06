package com.example.elonmars.presentation.extensions

import android.util.Log

fun Any.logError(text: String, exception: Exception) {
    Log.e(this::class.java.simpleName, text, exception)
}

fun Any.logError(text: String) {
    Log.e(this::class.java.simpleName, text)
}

fun Any.logDebug(text: String) {
    Log.d(this::class.java.simpleName, text)
}
