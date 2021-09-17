package com.example.elonmars.domain.provider

import com.example.elonmars.presentation.mission.model.TaskItem
import java.util.*

/** Интерфейс провайдера данных с информацией о задачах. */
interface ITaskItemsProvider {

    /**
     * Сохраняет задачу.
     *
     * @param taskItem задача, которую нужно сохранить.
     */
    fun saveTask(taskItem: TaskItem)

    /**
     * Возвращает задачи по конкретной дате.
     *
     * @param date дата, по которой нужно достать задачи.
     * @return список с моделями [TaskItem]
     */
    fun getTasksByDate(date: Calendar): List<TaskItem>

    /**
     * Обновляет статус задачи.
     *
     * @param taskItem задача, которая берется из базы данных.
     */
    fun updateTaskStatus(taskItem: TaskItem)

    /**
     * Удаляет задачу.
     *
     * @param taskItem задача, которую нужно удалить.
     */
    fun deleteTask(taskItem: TaskItem)
}
