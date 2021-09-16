package com.example.elonmars.domain

import com.example.elonmars.domain.interactors.HomeInteractor
import com.example.elonmars.domain.repositories.IHomeRepository
import com.example.elonmars.presentation.enums.TimerState
import io.mockk.*
import org.junit.Assert
import org.junit.Test

/** Класс для тестирования [HomeInteractor] */
class HomeInteractorTest {

    private var homeRepository: IHomeRepository = mockk()
    private var homeInteractor = HomeInteractor(homeRepository)

    @Test
    fun getEndMillisTest() {
        every { homeRepository.getEndMillis() } returns 1000L

        val result = homeInteractor.getEndMillis()

        verify(exactly = 1) { homeRepository.getEndMillis() }
        Assert.assertEquals(1000L, result)
    }

    @Test
    fun setEndMillisTest() {
        every { homeRepository.setEndMillis(1000L) } just Runs

        homeInteractor.setEndMillis(1000L)

        verify(exactly = 1) { homeRepository.setEndMillis(1000L) }
    }

    @Test
    fun setTimerStateTest() {
        every { homeRepository.setTimerState(TimerState.STARTED) } just Runs

        homeInteractor.setTimerState(TimerState.STARTED)

        verify(exactly = 1) { homeRepository.setTimerState(TimerState.STARTED) }
    }

    @Test
    fun getTimerStateTest() {
        every { homeRepository.getTimerState() } returns TimerState.STARTED.ordinal

        val result = homeInteractor.getTimerState()

        verify(exactly = 1) { homeRepository.getTimerState()  }
        Assert.assertEquals(TimerState.STARTED.ordinal, result)
    }
}
