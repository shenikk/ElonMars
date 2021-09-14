package com.example.elonmars.domain.interactors

import com.example.elonmars.presentation.model.TaskItem
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

interface ITaskInteractor {
    fun saveDataAsync(taskItem: TaskItem): Completable

    fun getDataAsync(date: Calendar): Single<List<TaskItem>>

    fun updateDataAsync(taskItem: TaskItem): Completable

    fun deleteTask(taskItem: TaskItem): Completable
}
