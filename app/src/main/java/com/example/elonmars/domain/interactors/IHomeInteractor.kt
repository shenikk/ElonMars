package com.example.elonmars.domain.interactors

import com.example.elonmars.presentation.enum.TimerState

interface IHomeInteractor {
    fun getEndMillis(): Long
    fun setEndMillis(endMillis: Long)
    fun setTimerState(timerState: TimerState)
    fun getTimerState(): Int
}
