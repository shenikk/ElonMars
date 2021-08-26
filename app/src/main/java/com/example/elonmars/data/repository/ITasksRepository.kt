package com.example.elonmars.data.repository

import com.example.elonmars.presentation.model.TaskItem
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

interface ITasksRepository {
    fun saveDataAsync(taskItem: TaskItem, date: Calendar): Completable
    fun getDataAsync(date: Calendar): Single<ArrayList<TaskItem>>
    fun updateDataAsync(taskItem: TaskItem, date: Calendar): Completable
}