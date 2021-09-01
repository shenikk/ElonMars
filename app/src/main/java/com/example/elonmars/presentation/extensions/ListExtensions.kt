package com.example.elonmars.presentation.extensions

import android.util.Log

fun <E> List<E>.getFirstItem(): E? {
    return try {
        this.first()
    } catch (e: Exception) {
        Log.e("List is empty", e.toString())
        null
    }
}
