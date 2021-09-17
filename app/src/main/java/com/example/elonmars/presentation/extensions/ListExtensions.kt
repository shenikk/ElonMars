package com.example.elonmars.presentation.extensions

/** Возвращает первые элемент из списка */
fun <E> List<E>.getFirstItem(): E? {
    return try {
        this.first()
    } catch (e: NoSuchElementException) {
        logError("List is empty", e)
        null
    }
}
