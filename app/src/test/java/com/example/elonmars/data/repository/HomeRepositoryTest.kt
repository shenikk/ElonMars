package com.example.elonmars.data.repository

import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.presentation.enum.TimerState
import io.mockk.*
import org.junit.Assert
import org.junit.Test

/** Класс для тестирования [HomeRepository] */
class HomeRepositoryTest {

    private var dataStorage: IDataStorage = mockk()
    private var homeRepository = HomeRepository(dataStorage)

    @Test
    fun getEndMillisTest() {
        every { dataStorage.endMillis } returns 10000L

        val result = homeRepository.getEndMillis()

        verify(exactly = 1) { dataStorage.endMillis }
        verify(exactly = 0) { dataStorage.endMillis = 100000L }
        Assert.assertEquals(10000L, result)
    }

    @Test
    fun setEndMillis() {
        every { dataStorage.endMillis = 100000L } just Runs

        homeRepository.setEndMillis(100000L)

        verify(exactly = 1) { dataStorage.endMillis = 100000L }
        verify(exactly = 0) { dataStorage.endMillis }
    }

    @Test
    fun setTimerStateTest() {
        every { dataStorage.timerState = TimerState.STARTED.ordinal } just Runs

        homeRepository.setTimerState(TimerState.STARTED)

        verify(exactly = 1) { dataStorage.timerState = TimerState.STARTED.ordinal }
        verify(exactly = 0) { dataStorage.timerState }
    }

    @Test
    fun getTimerStateTest() {
        every { dataStorage.timerState } returns TimerState.STARTED.ordinal

        val result = homeRepository.getTimerState()

        verify(exactly = 1) { dataStorage.timerState }
        Assert.assertEquals(TimerState.STARTED.ordinal, result)
    }
}
