package com.example.elonmars.data

interface Converter<T, R> {
    fun convert(item: T): R
    fun convert(item: ArrayList<R>): ArrayList<T>
}