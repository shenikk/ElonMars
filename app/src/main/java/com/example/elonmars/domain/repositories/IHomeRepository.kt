package com.example.elonmars.domain.repositories

import com.example.elonmars.presentation.enum.TimerState

/** Интерфейс репозитория с данными о конечном времени события. */
interface IHomeRepository {

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
