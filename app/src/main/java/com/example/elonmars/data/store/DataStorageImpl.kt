package com.example.elonmars.data.store

import android.content.SharedPreferences
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.model.TaskItem
import com.squareup.moshi.JsonAdapter

private const val TASKS_KEY = "TASKS_KEY"
private const val PHOTOS_KEY = "PHOTOS_KEY"

class DataStorageImpl(private val preferences: SharedPreferences,
                    private val jsonAdapter: JsonAdapter<ArrayList<PhotoItem>>
) : DataStorage {

//    override fun saveTasks(tasks: List<TaskItem>) {
//        if (tasks.isEmpty())
//            return
//
//        val result = jsonAdapter.toJson(tasks)
//
//        preferences.edit()
//            .putString(TASKS_KEY, result)
//            .apply()
//    }
//
//    override fun getTasks(): List<TaskItem>? {
//        return preferences.getString(TASKS_KEY, null)
//            ?.let(jsonAdapter::fromJson)
//    }

    override fun savePhotos(photos: ArrayList<PhotoItem>) {
        if (photos.isEmpty())
            return

        val result = jsonAdapter.toJson(photos)

        preferences.edit()
            .putString(PHOTOS_KEY, result)
            .apply()
    }

    override fun getPhotos(): ArrayList<PhotoItem>? {
        return preferences.getString(PHOTOS_KEY, null)
            ?.let(jsonAdapter::fromJson)
    }
}