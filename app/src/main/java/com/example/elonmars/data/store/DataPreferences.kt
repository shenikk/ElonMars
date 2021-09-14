package com.example.elonmars.data.store

import com.google.gson.Gson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

abstract class DataPreferences {

    /** Метод для парсинга данных из формата Json */
    protected inline fun <reified T> getDataFromJson(input: String): List<T> {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val type: Type = Types.newParameterizedType(MutableList::class.java, T::class.java)
        val jsonAdapter: JsonAdapter<List<T>> = moshi.adapter(type)

        return jsonAdapter.fromJson(input)!!
    }

    /** Метод для парсинга данных в формат Json */
    protected inline fun <reified T> convertDataToJson(input: List<T>?): String? {
        return input?.let {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val type: Type = Types.newParameterizedType(MutableList::class.java, T::class.java)
            val jsonAdapter: JsonAdapter<List<T>> = moshi.adapter(type)

            return jsonAdapter.toJson(it)
        }
    }

    protected inline fun <reified T> getItemFromJson(input: String): T {
        val gson = Gson()
        return gson.fromJson(input, T::class.java)

    }

    protected inline fun <reified T> convertItemToJson(input: T?): String? {
        val gson = Gson()
        return gson.toJson(input)
    }
}
