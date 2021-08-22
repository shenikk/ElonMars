package com.example.elonmars.data.store

import android.content.SharedPreferences
import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.model.TaskItem
import com.example.elonmars.presentation.model.WeatherItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

private const val TASKS_KEY = "TASKS_KEY"
private const val PHOTOS_KEY = "PHOTOS_KEY"
private const val WEATHER_KEY = "WEATHER_KEY"

class DataStorageImpl(private val preferences: SharedPreferences) : DataStorage {

//    override fun saveTasks(tasks: ArrayList<TaskItem>) {
//        if (tasks.isEmpty())
//            return
//
//        val result = convertDataToJson(tasks)
//
//        preferences.edit()
//            .putString(TASKS_KEY, result)
//            .apply()
//    }
//
//    override fun getTasks(): ArrayList<TaskItem>? {
//        return preferences.getString(TASKS_KEY, null)
//            ?.let {
//                getDataFromJson(it)
//            }
//    }

    override fun saveWeatherDates(dates: ArrayList<WeatherDataItem>) {
        if (dates.isEmpty())
            return

        val result = convertDataToJson(dates)

        preferences.edit()
            .putString(WEATHER_KEY, result)
            .apply()
    }

    override fun getWeatherDates(): ArrayList<WeatherDataItem>? {
        return preferences.getString(WEATHER_KEY, null)
            ?.let {
                getDataFromJson(it)
            }
    }

    override fun savePhotos(photos: ArrayList<PhotoItem>) {
        if (photos.isEmpty())
            return

        val result = convertDataToJson(photos)

        preferences.edit()
            .putString(PHOTOS_KEY, result)
            .apply()
    }

    override fun getPhotos(): ArrayList<PhotoItem>? {
        return preferences.getString(PHOTOS_KEY, null)
            ?.let {
                getDataFromJson(it)
            }
    }

    private inline fun <reified T> getDataFromJson(input: String): ArrayList<T> {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val type: Type = Types.newParameterizedType(MutableList::class.java, T::class.java)
        val jsonAdapter: JsonAdapter<ArrayList<T>> = moshi.adapter(type)

        return jsonAdapter.fromJson(input)!!
    }

    private inline fun <reified T> convertDataToJson(input: ArrayList<T>): String {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val type: Type = Types.newParameterizedType(MutableList::class.java, T::class.java)
        val jsonAdapter: JsonAdapter<ArrayList<T>> = moshi.adapter(type)

        return jsonAdapter.toJson(input)!!
    }
}