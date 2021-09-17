package com.example.elonmars.presentation.mission.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.domain.interactors.ITaskInteractor
import com.example.elonmars.domain.provider.ISchedulersProvider
import com.example.elonmars.presentation.extensions.logDebug
import com.example.elonmars.presentation.mission.model.TaskItem
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * ViewModel экрана со списком задач, добавляемые пользователем.
 *
 * @param tasksInteractor интерактор с данными о задачах.
 * @param schedulersProvider провайдер с Scheduler для работы на разных потоках.
 *
 * @testClass unit: MarsMissionViewModelTest
 */
class MarsMissionViewModel(
    private val tasksInteractor: ITaskInteractor,
    private val schedulersProvider: ISchedulersProvider
) : ViewModel() {

    private var disposable: Disposable? = null

    private val errorLiveData = MutableLiveData<Throwable>()
    private val taskItemsLiveData = MutableLiveData<List<TaskItem>>()

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        logDebug("onCleared() called")
    }

    /**
     * Добавляет задачу в базу данных.
     *
     * @param taskItem задача, которая берется из базы данных.
     */
    fun addTaskItemToDataBase(taskItem: TaskItem) {
        disposable = tasksInteractor.saveDataAsync(taskItem)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe()
    }

    /**
     * Достает задачи из базы данных.
     *
     * @param date дата, по которой нужно достать задачи.
     */
    fun getTaskItemFromDataBase(date: Calendar) {
        disposable = tasksInteractor.getDataAsync(date)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe(taskItemsLiveData::setValue, errorLiveData::setValue)
    }

    /**
     * Обновляет статус задачи в базе данных.
     *
     * @param taskItem задача, которая берется из базы данных.
     */
    fun updateTaskStatus(taskItem: TaskItem) {
        disposable = tasksInteractor.updateDataAsync(taskItem)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe()
    }

    /**
     * Удаляет задачу из базы данных.
     *
     * @param taskItem задача, которую нужно удалить из базы данных.
     */
    fun deleteTaskItemFromDataBase(taskItem: TaskItem) {
        disposable = tasksInteractor.deleteTask(taskItem)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe()
    }

    /**
     * Метод для получения инстанса LiveData со списком задач.
     *
     * @return LiveData со списком задач.
     */
    fun getTaskItemLiveData(): LiveData<List<TaskItem>> {
        return taskItemsLiveData
    }

    /**
     * Метод для получения инстанса LiveData с ошибкой.
     *
     * @return LiveData с ошибкой [Throwable].
     */
    fun getErrorLiveData(): LiveData<Throwable> {
        return errorLiveData
    }
}
