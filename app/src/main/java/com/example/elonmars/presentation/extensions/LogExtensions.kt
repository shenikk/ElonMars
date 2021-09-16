package com.example.elonmars.presentation.extensions

import android.util.Log

/**
 * Логирует сообщение об ошибке.
 *
 * @param text сообщение с информацией, которое будет показано в логах.
 * @param exception исключение.
 */
fun Any.logError(text: String, exception: Exception) {
    Log.e(this::class.java.simpleName, text, exception)
}

/**
 * Логирует сообщение об ошибке.
 *
 * @param text сообщение с информацией, которое будет показано в логах.
 */
fun Any.logError(text: String) {
    Log.e(this::class.java.simpleName, text)
}

/**
 * Логирует сообщение для дебага.
 *
 * @param text сообщение с информацией, которое будет показано в логах.
 */
fun Any.logDebug(text: String) {
    Log.d(this::class.java.simpleName, text)
}
