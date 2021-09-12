package com.example.elonmars.domain.repositories

import com.example.elonmars.presentation.enum.TimerState

interface IHomeRepository {
    fun getEndMillis(): Long

    fun setEndMillis(endMillis: Long)

    fun setTimerState(timerState: TimerState)

    fun getTimerState(): Int
}