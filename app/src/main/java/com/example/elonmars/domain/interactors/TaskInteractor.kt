package com.example.elonmars.domain.interactors

import com.example.elonmars.data.repository.TasksRepository
import com.example.elonmars.presentation.model.TaskItem
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

class TaskInteractor(private val tasksRepository: TasksRepository) : ITaskInteractor {

    override fun saveDataAsync(taskItem: TaskItem): Completable = tasksRepository.saveDataAsync(taskItem)

    override fun getDataAsync(date: Calendar): Single<ArrayList<TaskItem>> = tasksRepository.getDataAsync(date)

    override fun updateDataAsync(taskItem: TaskItem): Completable = tasksRepository.updateDataAsync(taskItem)

    override fun deleteTask(taskItem: TaskItem): Completable = tasksRepository.deleteTask(taskItem)
}