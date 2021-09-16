package com.example.elonmars.domain.repositories

import com.example.elonmars.presentation.model.TaskItem
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

/** Интерфейс репозитория с задачами */
interface ITasksRepository {

    /**
     * Сохраняет задачу.
     *
     * @param taskItem задача, которую нужно сохранить.
     * @return Completable
     */
    fun saveDataAsync(taskItem: TaskItem): Completable

    /**
     * Возвращает задачи по конкретной дате.
     *
     * @param date дата, по которой нужно достать задачи.
     * @return Single со списком моделей [TaskItem].
     */
    fun getDataAsync(date: Calendar): Single<List<TaskItem>>

    /**
     * Обновляет статус задачи в базе данных.
     *
     * @param taskItem задача, которую нужно обновить.
     * @return Completable
     */
    fun updateDataAsync(taskItem: TaskItem): Completable


    /**
     * Удаляет задачу.
     *
     * @param taskItem задача, которую нужно удалить.
     * @return Completable
     */
    fun deleteTask(taskItem: TaskItem): Completable
}
