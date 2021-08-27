package com.example.elonmars.data

import com.example.elonmars.data.model.TaskEntity
import com.example.elonmars.presentation.model.TaskItem

class ConverterImpl: Converter<TaskItem, TaskEntity> {

    override fun convert(item: TaskItem): TaskEntity {
        return TaskEntity(item.dayOfMonth, item.month, item.title, item.isCompleted)
    }

    override fun convert(items: ArrayList<TaskEntity>): ArrayList<TaskItem> {
        val result = arrayListOf<TaskItem>()
        items.forEach { item ->
            val taskItem = TaskItem(item.title, item.isCompleted, item.dayOfMonth, item.month)
            result.add(taskItem)
        }
        return result
    }
}