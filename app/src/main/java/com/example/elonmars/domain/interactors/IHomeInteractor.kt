package com.example.elonmars.domain.interactors

import com.example.elonmars.presentation.enums.TimerState

/** Интерфейс интерактора для главного экрана. */
interface IHomeInteractor {

    /**
     * Возвращет время до события.
     *
     * @return время в миллисекундах.
     */
    fun getEndMillis(): Long

    /**
     * Устанавливает время до события.
     *
     * @param endMillis время в миллисекундах.
     */
    fun setEndMillis(endMillis: Long)

    /**
     * Устанавливает состояние таймера.
     * Состояние таймера представлено порядковым номером константы перечисления [TimerState].
     *
     * @param timerState состояние таймера.
     */
    fun setTimerState(timerState: TimerState)

    /**
     * Возвращает состояние таймера.
     * Состояние таймера представлено порядковым номером константы перечисления [TimerState].
     *
     * @return состояние таймера.
     */
    fun getTimerState(): Int
}
