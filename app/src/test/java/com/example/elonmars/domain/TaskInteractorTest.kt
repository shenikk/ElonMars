package com.example.elonmars.domain

import com.example.elonmars.domain.interactors.TaskInteractor
import com.example.elonmars.domain.repositories.ITasksRepository
import com.example.elonmars.presentation.mission.model.TaskItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test
import java.util.*

/** Класс для тестирования [TaskInteractor] */
class TaskInteractorTest {

    private val taskRepository: ITasksRepository = mockk()
    private val taskInteractor = TaskInteractor(taskRepository)
    private val taskItem = TaskItem("title", false, 31, 2)

    @Test
    fun saveDataAsyncTest() {
        every { taskRepository.saveDataAsync(taskItem) } returns Completable.complete()

        val testObserver = taskInteractor.saveDataAsync(taskItem).test()

        verify(exactly = 1) { taskRepository.saveDataAsync(taskItem) }
        testObserver.assertResult()
        testObserver.dispose()
    }

    @Test
    fun getDataAsyncTest() {
        val date = Calendar.getInstance()
        val taskItem = TaskItem("title", false, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH))
        every { taskRepository.getDataAsync(date) } returns Single.just(listOf(taskItem))

        val testObserver = taskInteractor.getDataAsync(date).test()

        verify(exactly = 1) { taskRepository.getDataAsync(date) }
        testObserver.assertResult(listOf(taskItem))
        testObserver.assertValueAt(0, listOf(taskItem))
        testObserver.assertNever(listOf())
        testObserver.dispose()
    }

    @Test
    fun updateDataAsyncTest() {
        val updatedTaskItem = TaskItem("title", true, 31, 2)
        every { taskRepository.updateDataAsync(updatedTaskItem) } returns Completable.complete()

        val testObserver = taskInteractor.updateDataAsync(updatedTaskItem).test()

        verify(exactly = 1) { taskRepository.updateDataAsync(updatedTaskItem) }
        testObserver.assertResult()
        testObserver.dispose()
    }

    @Test
    fun deleteTaskTest() {
        every { taskRepository.deleteTask(taskItem) } returns Completable.complete()

        val testObserver = taskInteractor.deleteTask(taskItem).test()

        verify(exactly = 1) { taskRepository.deleteTask(taskItem) }
        testObserver.assertResult()
        testObserver.dispose()
    }
}
