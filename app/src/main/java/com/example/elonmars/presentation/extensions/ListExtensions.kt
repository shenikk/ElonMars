package com.example.elonmars.presentation.extensions

fun <E> List<E>.getFirstItem(): E? {
    return try {
        this.first()
    } catch (e: Exception) {
        logError("List is empty", e)
        null
    }
}
