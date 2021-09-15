package com.example.elonmars.presentaton.viewModelTest

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.elonmars.domain.provider.ISchedulersProvider
import com.example.elonmars.domain.interactors.ITaskInteractor
import com.example.elonmars.presentation.model.TaskItem
import com.example.elonmars.presentation.viewmodel.MarsMissionViewModel
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

/** Класс для тестирования [MarsMissionViewModel] */
class MarsMissionViewModelTest {

    @get:Rule
    var mRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var marsMissionViewModel: MarsMissionViewModel
    private var errorLiveDataObserver: Observer<Throwable> = mockk()
    private var taskItemsLiveDataObserver: Observer<List<TaskItem>> = mockk()

    private val schedulersProvider: ISchedulersProvider = mockk()
    private val taskInteractor: ITaskInteractor = mockk()

    private val date = Calendar.getInstance()
    private val taskItem = TaskItem("title", false, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH))

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        marsMissionViewModel = MarsMissionViewModel(taskInteractor, schedulersProvider)
        marsMissionViewModel.getErrorLiveData().observeForever(errorLiveDataObserver)
        marsMissionViewModel.getTaskItemLiveData().observeForever(taskItemsLiveDataObserver)

        every { Log.e(any(), any()) } returns 0
        every { schedulersProvider.io() } returns Schedulers.trampoline()
        every { schedulersProvider.ui() } returns Schedulers.trampoline()

        every { errorLiveDataObserver.onChanged(any()) } just Runs
        every { taskItemsLiveDataObserver.onChanged(any()) } just Runs
    }

    @Test
    fun addTaskItemToDataBaseTest() {
        // Arrange
        every { taskInteractor.saveDataAsync(taskItem) } returns Completable.complete()

        // Act
        marsMissionViewModel.addTaskItemToDataBase(taskItem)

        // Assert
        verify(exactly = 1) { taskInteractor.saveDataAsync(taskItem) }
        verify(exactly = 0)  { taskItemsLiveDataObserver.onChanged(any()) }
        verify { errorLiveDataObserver wasNot called }
    }

    @Test
    fun getTaskItemFromDataBaseTest() {
        // Arrange
        every { taskInteractor.saveDataAsync(taskItem) } returns Completable.complete()
        every { taskInteractor.getDataAsync(date) } returns Single.just(arrayListOf(taskItem))
        marsMissionViewModel.addTaskItemToDataBase(taskItem)

        // Act
        marsMissionViewModel.getTaskItemFromDataBase(date)

        // Assert
        verify(exactly = 1) { taskInteractor.getDataAsync(date) }
        verify(exactly = 1)  { taskItemsLiveDataObserver.onChanged(any()) }
        verify { errorLiveDataObserver wasNot called }

        Assert.assertEquals(taskItem, marsMissionViewModel.getTaskItemLiveData().value?.get(0))
    }

    @Test
    fun updateTaskStatusTest() {
        // Arrange
        val updatedTaskItem = TaskItem("title", true, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH))

        every { taskInteractor.saveDataAsync(taskItem) } returns Completable.complete()
        every { taskInteractor.updateDataAsync(updatedTaskItem) } returns Completable.complete()
        every { taskInteractor.getDataAsync(date) } returns Single.just(arrayListOf(updatedTaskItem))
        marsMissionViewModel.addTaskItemToDataBase(taskItem)

        // Act
        marsMissionViewModel.updateTaskStatus(updatedTaskItem)

        // Assert
        verify(exactly = 1) { taskInteractor.updateDataAsync(updatedTaskItem) }
        verify(exactly = 0)  { taskItemsLiveDataObserver.onChanged(any()) }
        verify { errorLiveDataObserver wasNot called }

        marsMissionViewModel.getTaskItemFromDataBase(date)
        Assert.assertEquals(updatedTaskItem, marsMissionViewModel.getTaskItemLiveData().value?.get(0))
    }

    @Test
    fun deleteTaskItemFromDataBaseTest() {
        // Arrange
        every { taskInteractor.saveDataAsync(taskItem) } returns Completable.complete()
        every { taskInteractor.deleteTask(taskItem) } returns Completable.complete()
        marsMissionViewModel.addTaskItemToDataBase(taskItem)

        // Act
        marsMissionViewModel.deleteTaskItemFromDataBase(taskItem)

        // Assert
        verify(exactly = 1) { taskInteractor.deleteTask(taskItem) }
        verify(exactly = 0)  { taskItemsLiveDataObserver.onChanged(any()) }
        verify { errorLiveDataObserver wasNot called }
    }

    /** Delete taskItem which doesn't exist, no error is thrown */
    @Test
    fun deleteTaskItemFromDataBaseNoFailTest() {
        // Arrange
        every { taskInteractor.deleteTask(taskItem) } returns Completable.complete()

        // Act
        marsMissionViewModel.deleteTaskItemFromDataBase(taskItem)

        // Assert
        verify(exactly = 1) { taskInteractor.deleteTask(taskItem) }
        verify(exactly = 0)  { taskItemsLiveDataObserver.onChanged(any()) }
        verify { errorLiveDataObserver wasNot called }
    }
}