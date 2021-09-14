package com.example.elonmars.domain.provider

import com.example.elonmars.presentation.model.TaskItem
import java.util.*

interface ITaskItemsProvider {
    fun saveTask(taskItem: TaskItem)
    fun getTasksByDate(date: Calendar): List<TaskItem>
    fun updateTaskStatus(taskItem: TaskItem)
    fun deleteTask(taskItem: TaskItem)
}
