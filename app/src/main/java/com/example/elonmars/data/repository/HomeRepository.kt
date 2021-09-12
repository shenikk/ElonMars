package com.example.elonmars.domain.interactors

import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.repositories.IHomeRepository
import com.example.elonmars.presentation.enum.TimerState

class HomeRepository(private val dataStorage: IDataStorage): IHomeRepository {

    override fun getEndMillis() = dataStorage.endMillis

    override fun setEndMillis(endMillis: Long) {
        dataStorage.endMillis = endMillis
    }

    override fun setTimerState(timerState: TimerState) {
        dataStorage.timerState = timerState.ordinal
    }

    override fun getTimerState() = dataStorage.timerState
}
