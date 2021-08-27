package com.example.elonmars.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.data.provider.ISchedulersProvider
import com.example.elonmars.data.repository.TasksRepository
import com.example.elonmars.presentation.model.TaskItem
import io.reactivex.disposables.Disposable
import java.util.*

class MarsMissionViewModel(
    private val tasksRepository: TasksRepository,
    private val schedulersProvider: ISchedulersProvider
) : ViewModel() {

    private var disposable: Disposable? = null

    private val shimmerLiveData = MutableLiveData<Boolean>()
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
        disposable = tasksRepository.saveDataAsync(taskItem)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe()
    }

    fun getTaskItemFromDataBase(date: Calendar) {
        disposable = tasksRepository.getDataAsync(date)
            .doOnSubscribe {
                shimmerLiveData.postValue(true)
            }
            .doAfterTerminate { shimmerLiveData.postValue(false) }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe(taskItemsLiveData::setValue, errorLiveData::setValue)
    }

    fun updateTaskStatus(taskItem: TaskItem) {
        disposable = tasksRepository.updateDataAsync(taskItem)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe()
    }

    fun deleteTaskItemFromDataBase(taskItem: TaskItem) {
        disposable = tasksRepository.deleteTask(taskItem)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe()
    }

    fun getTaskItemLiveData(): LiveData<ArrayList<TaskItem>> {
        return taskItemsLiveData
    }
}