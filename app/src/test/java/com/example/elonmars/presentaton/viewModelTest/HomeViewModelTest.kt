package com.example.elonmars.presentaton.viewModelTest

import com.example.elonmars.domain.interactors.IHomeInteractor
import com.example.elonmars.presentation.enum.TimerState
import com.example.elonmars.presentation.viewmodel.HomeViewModel
import io.mockk.*
import org.junit.Assert
import org.junit.Test

/** Класс для тестирования [HomeViewModel] */
class HomeViewModelTest {

    private val homeInteractor: IHomeInteractor = mockk()
    private val homeViewModel = HomeViewModel(homeInteractor)
    private val endMillis = 1000L

    @Test
    fun getTimerStateTest() {
        val timerState = TimerState.NOT_STARTED.ordinal
        every { homeInteractor.getTimerState() } returns timerState

        val result = homeViewModel.getTimerState()

        verify(exactly = 1) { homeInteractor.getTimerState() }
        Assert.assertEquals(timerState, result)
    }

    @Test
    fun setEndMillisTest() {
        every { homeInteractor.setEndMillis(endMillis) } just Runs

        homeViewModel.setEndMillis(endMillis)

        verify(exactly = 1) { homeInteractor.setEndMillis(endMillis)  }
    }

    @Test
    fun getEndMillisTest() {
        every { homeInteractor.getEndMillis() } returns endMillis

        val result = homeViewModel.getEndMillis()

        verify(exactly = 1) { homeInteractor.getEndMillis() }
        Assert.assertEquals(endMillis, result)
    }
}
