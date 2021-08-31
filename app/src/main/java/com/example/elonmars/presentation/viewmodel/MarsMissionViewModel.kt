package com.example.elonmars.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.R
import com.example.elonmars.data.provider.ISchedulersProvider
import com.example.elonmars.domain.interactors.ITaskInteractor
import com.example.elonmars.presentation.model.TaskItem
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * ViewModel экрана со списком задач, добавляемые пользователем.
 *
 * @param tasksInteractor интерактор с данными о задачах
 * @param schedulersProvider
 *
 * @testClass unit: MarsMissionViewModelTest
 */
class MarsMissionViewModel(
    private val tasksInteractor: ITaskInteractor,
    private val schedulersProvider: ISchedulersProvider
) : ViewModel() {

    private var disposable: Disposable? = null

    private val errorLiveData = MutableLiveData<Throwable>()
    private val taskItemsLiveData = MutableLiveData<ArrayList<TaskItem>>()

    companion object {
        private const val TAG = "MarsMissionViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        Log.d(TAG, "onCleared() called")
    }

    fun addTaskItemToDataBase(taskItem: TaskItem) {
        disposable = tasksInteractor.saveDataAsync(taskItem)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe()
    }

    fun getTaskItemFromDataBase(date: Calendar) {
        disposable = tasksInteractor.getDataAsync(date)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe(taskItemsLiveData::setValue, errorLiveData::setValue)
    }

    fun updateTaskStatus(taskItem: TaskItem) {
        disposable = tasksInteractor.updateDataAsync(taskItem)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe()
    }

    fun deleteTaskItemFromDataBase(taskItem: TaskItem) {
        disposable = tasksInteractor.deleteTask(taskItem)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe()
    }

    fun getTaskItemLiveData(): LiveData<ArrayList<TaskItem>> {
        return taskItemsLiveData
    }

    fun getErrorLiveData(): LiveData<Throwable> {
        return errorLiveData
    }
}