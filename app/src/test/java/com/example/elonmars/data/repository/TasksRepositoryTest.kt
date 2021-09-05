package com.example.elonmars.data.repository

import com.example.elonmars.data.provider.TaskItemsProvider
import com.example.elonmars.presentation.model.TaskItem
import io.mockk.*
import org.junit.Test
import java.util.*

/** Класс для тестирования [TasksRepository] */
class TasksRepositoryTest {

    private var taskItemsProvider: TaskItemsProvider = mockk()
    private var tasksRepository = TasksRepository(taskItemsProvider)

    @Test
    fun saveDataAsyncTest() {
        // Arrange
        val taskItem = TaskItem("title", false, 31, 2)
        every { taskItemsProvider.saveTask(taskItem) } just runs

        // Act
        val testObserver = tasksRepository.saveDataAsync(taskItem).test()

        // Assert
        verify(exactly = 1) { taskItemsProvider.saveTask(taskItem) }
        testObserver.assertResult()
        testObserver.dispose()
    }

    @Test
    fun getDataAsyncTest() {
        // Arrange
        val date = Calendar.getInstance()
        val taskItem = TaskItem("title", false, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH))
        every { taskItemsProvider.saveTask(taskItem) } just runs
        every { taskItemsProvider.getTasksByDate(date) } returns arrayListOf(taskItem)

        tasksRepository.saveDataAsync(taskItem)

        // Act
        val testObserver = tasksRepository.getDataAsync(date).test()

        // Assert
        verify(exactly = 1) { taskItemsProvider.getTasksByDate(date) }
        testObserver.assertResult(arrayListOf(taskItem))
        testObserver.assertValueAt(0, arrayListOf(taskItem))
        testObserver.assertNever(arrayListOf())
        testObserver.dispose()
    }

    @Test
    fun updateDataAsyncTest() {
        // Arrange
        val taskItem = TaskItem("title", false, 31, 2)
        val updatedTaskItem = TaskItem("title", true, 31, 2)
        every { taskItemsProvider.saveTask(taskItem) } just runs
        every { taskItemsProvider.updateTaskStatus(updatedTaskItem) } just runs

        tasksRepository.saveDataAsync(taskItem)

        // Act
        val testObserver = tasksRepository.updateDataAsync(updatedTaskItem).test()

        // Assert
        verify(exactly = 1) { taskItemsProvider.updateTaskStatus(updatedTaskItem) }
        testObserver.assertResult()
        testObserver.dispose()
    }

    @Test
    fun deleteTaskTest() {
        // Arrange
        val taskItem = TaskItem("title", false, 31, 2)
        every { taskItemsProvider.saveTask(taskItem) } just runs
        every { taskItemsProvider.deleteTask(taskItem) } just runs

        tasksRepository.saveDataAsync(taskItem)

        // Act
        val testObserver = tasksRepository.deleteTask(taskItem).test()

        // Assert
        verify(exactly = 1) { taskItemsProvider.deleteTask(taskItem) }
        testObserver.assertResult()
        testObserver.dispose()
    }
}
