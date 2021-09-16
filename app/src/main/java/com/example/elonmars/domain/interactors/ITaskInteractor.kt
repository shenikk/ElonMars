package com.example.elonmars.domain.interactors

import com.example.elonmars.presentation.model.TaskItem
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

/** Интерфейс интерактора для экрана с задачами. */
interface ITaskInteractor {

    /**
     * Асихронно сохраняет модель данных [TaskItem].
     *
     * @return Completable
     */
    fun saveDataAsync(taskItem: TaskItem): Completable

    /**
     * Асихронно возвращает Single со списком моделей данных [TaskItem].
     *
     * @param date дата, по которой нужно достать задачи.
     * @return Single со списком моделей данных [TaskItem].
     */
    fun getDataAsync(date: Calendar): Single<List<TaskItem>>

    /**
     * Асихронно обновляет статус задачи.
     *
     * @param taskItem задача, которую нужно обновить.
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
