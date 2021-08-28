package com.example.elonmars.data.repository

import com.example.elonmars.data.converters.ConverterImpl
import com.example.elonmars.data.database.TaskDao
import com.example.elonmars.presentation.model.TaskItem
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import kotlin.collections.ArrayList

class TasksRepository(private val converter: ConverterImpl, private val taskDao: TaskDao) : ITasksRepository {

    override fun saveDataAsync(taskItem: TaskItem): Completable {
        val taskEntity = converter.convert(taskItem)
        return Completable.fromCallable { taskDao.saveTask(taskEntity) }
    }

    override fun getDataAsync(date: Calendar): Single<ArrayList<TaskItem>> {
        return Single.fromCallable {
            val tasksList = ArrayList(taskDao.getTasksByDate(date.get(Calendar.DAY_OF_MONTH).toString(), date.get(Calendar.MONTH).toString()))
            converter.convert(tasksList)
        }
    }

    override fun updateDataAsync(taskItem: TaskItem): Completable {
        val taskEntity = converter.convert(taskItem)
        return Completable.fromCallable { taskDao.updateTaskStatus(taskEntity) }
    }

    override fun deleteTask(taskItem: TaskItem): Completable {
        val taskEntity = converter.convert(taskItem)
        return Completable.fromRunnable { taskDao.deleteTask(taskEntity) }
    }
}