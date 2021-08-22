package com.example.elonmars.data.store

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.model.TaskItem

/** Интерфейс хранилища списка фильмов */
interface DataStorage {

//    /** Метод для сохранения списка задач в [android.content.SharedPreferences] */
//    fun saveTasks(movies: List<TaskItem>)
//
//    /** Метод для извлечения списка задач из [android.content.SharedPreferences] */
//    fun getTasks(): List<TaskItem>?

    fun savePhotos(photos: ArrayList<PhotoItem>)

    fun getPhotos(): ArrayList<PhotoItem>?
}