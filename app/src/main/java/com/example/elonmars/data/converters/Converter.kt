package com.example.elonmars.data.converters

interface Converter<T, R> {
    fun convert(item: T): R
    fun convert(item: ArrayList<R>): ArrayList<T>
}