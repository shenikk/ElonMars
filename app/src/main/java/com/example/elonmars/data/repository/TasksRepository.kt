package com.example.elonmars.data.repository

import com.example.elonmars.data.provider.TaskItemsProvider
import com.example.elonmars.presentation.model.TaskItem
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

class TasksRepository(private val taskItemsProvider: TaskItemsProvider) : ITasksRepository {

    override fun saveDataAsync(taskItem: TaskItem, date: Calendar): Completable {
        return Completable.fromCallable { taskItemsProvider.saveTask(taskItem, date) }
    }

    override fun getDataAsync(date: Calendar): Single<ArrayList<TaskItem>> {
        return Single.fromCallable { taskItemsProvider.getTasksByDate(date) }
    }

    override fun updateDataAsync(taskItem: TaskItem, date: Calendar): Completable {
        return Completable.fromCallable { taskItemsProvider.updateTaskStatus(taskItem, date) }
    }
}