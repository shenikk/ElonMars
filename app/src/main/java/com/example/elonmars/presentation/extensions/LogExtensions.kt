package com.example.elonmars.presentation.extensions

import android.util.Log

fun Any.LogError(text: String, exception: Exception) {
    Log.e(this::class.java.simpleName, text, exception)
}

fun Any.LogError(text: String) {
    Log.e(this::class.java.simpleName, text)
}

fun Any.LogDebug(text: String) {
    Log.d(this::class.java.simpleName, text)
}