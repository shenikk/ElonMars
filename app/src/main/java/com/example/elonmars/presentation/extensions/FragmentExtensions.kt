package com.example.elonmars.presentation.extensions

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


/**
 * Добавляет снекбар
 *
 * @param text текст, который будет показан в снеке
 * @param length время показа снека
 */
fun Fragment.showSnackbar(text: String, length: Int = Snackbar.LENGTH_SHORT) {
    view?.run { Snackbar.make(this, text, length).show() }
}

/**
 * Добавляет снекбар
 *
 * @param text текст, который будет показан в снеке
 * @param length время показа снека
 */
fun Fragment.showSnackbar(@StringRes text: Int, length: Int = Snackbar.LENGTH_SHORT) {
    view?.run { Snackbar.make(this, text, length).show() }
}
