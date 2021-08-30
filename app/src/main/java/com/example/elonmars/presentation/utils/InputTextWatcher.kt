package com.example.elonmars.presentation.utils

import android.text.Editable
import android.text.TextWatcher

/** Класс для мониторинга изменений текста в TextInputLayout */
class InputTextWatcher(val afterTextChanged: () -> Unit) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChanged()
    }
}