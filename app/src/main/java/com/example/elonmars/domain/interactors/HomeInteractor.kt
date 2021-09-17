package com.example.elonmars.domain.interactors

import com.example.elonmars.domain.repositories.IHomeRepository
import com.example.elonmars.presentation.home.enums.TimerState

/**
 * Интерактор для главного экрана приложения.
 * Устанавливает и получает состояние таймера,
 * а также получает и устанавливает конечное время события.
 *
 * @param homeRepository хранилище данных
 *
 * @testClass unit: HomeInteractorTest
 */
class HomeInteractor(private val homeRepository: IHomeRepository) : IHomeInteractor {

    override fun getEndMillis(): Long {
        return homeRepository.getEndMillis()
    }

    override fun setEndMillis(endMillis: Long) {
        homeRepository.setEndMillis(endMillis)
    }

    override fun setTimerState(timerState: TimerState) {
        homeRepository.setTimerState(timerState)
    }

    override fun getTimerState(): Int {
        return homeRepository.getTimerState()
    }
}
