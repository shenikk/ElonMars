package com.example.elonmars.data.repository

import com.example.elonmars.domain.provider.ITaskItemsProvider
import com.example.elonmars.domain.repositories.ITasksRepository
import com.example.elonmars.presentation.mission.model.TaskItem
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

/**
 * Репозиторий - провайдер данных о задачах.
 *
 * @param taskItemsProvider провайдер данных задач
 *
 * @testClass unit: TasksRepositoryTest
 */
class TasksRepository(private val taskItemsProvider: ITaskItemsProvider) : ITasksRepository {

    override fun saveDataAsync(taskItem: TaskItem): Completable {
        return Completable.fromCallable { taskItemsProvider.saveTask(taskItem) }
    }

    override fun getDataAsync(date: Calendar): Single<List<TaskItem>> {
        return Single.fromCallable { taskItemsProvider.getTasksByDate(date) }
    }

    override fun updateDataAsync(taskItem: TaskItem): Completable {
        return Completable.fromCallable { taskItemsProvider.updateTaskStatus(taskItem) }
    }

    override fun deleteTask(taskItem: TaskItem): Completable {
        return Completable.fromRunnable { taskItemsProvider.deleteTask(taskItem) }
    }
}
