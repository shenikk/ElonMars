package com.example.elonmars.presentation.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.domain.interactors.IHomeInteractor
import com.example.elonmars.presentation.enums.TimerState
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * ViewModel главного экрана.
 *
 * @param homeInteractor интерактор главного экрана.
 *
 * @testClass unit: HomeViewModelTest
 */
class HomeViewModel(private val homeInteractor: IHomeInteractor) : ViewModel() {

    private val timerText = MutableLiveData<String>()
    private val timerState = MutableLiveData<Int>()
    private var timer: CountDownTimer? = null

    companion object {
        private const val COUNT_DOWN_INTERVAL = 1000L
    }

    /** Запускает таймер с обратным отсчетом до выбранной даты и времени */
    fun startTimer() {
        val currentMillis = Calendar.getInstance().timeInMillis
        val endMillis = homeInteractor.getEndMillis()
        val totalMillis = endMillis - currentMillis

        timer = object : CountDownTimer(totalMillis, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {

                var millisUntilFinish = millisUntilFinished
                val days: Long = TimeUnit.MILLISECONDS.toDays(millisUntilFinish)

                millisUntilFinish -= TimeUnit.DAYS.toMillis(days)
                val hours: Long = TimeUnit.MILLISECONDS.toHours(millisUntilFinish)

                millisUntilFinish -= TimeUnit.HOURS.toMillis(hours)
                val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinish)

                millisUntilFinish -= TimeUnit.MINUTES.toMillis(minutes)
                val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinish)

                timerText.value = "$days days ${hours}h ${minutes}m ${seconds}s left"
            }

            override fun onFinish() {
                homeInteractor.setTimerState(TimerState.NOT_STARTED)
                timerState.value = TimerState.NOT_STARTED.ordinal
            }
        }
        timer?.start()

        homeInteractor.setTimerState(TimerState.STARTED)
    }

    /** Отменяет таймер с обратным отсчетом до выбранной даты и времени */
    fun cancelTimer() {
        timer?.cancel()
    }

    /** Возвращает состояние таймера */
    fun getTimerState() = homeInteractor.getTimerState()

    /** Устанавливает время до события в миллисекундах */
    fun setEndMillis(endMillis: Long) {
        homeInteractor.setEndMillis(endMillis)
    }

    /** Возвращает время до события в миллисекундах */
    fun getEndMillis() = homeInteractor.getEndMillis()

    /**
     * Метод для получения инстанса LiveData с тектом.
     *
     * @return LiveData с текстом.
     */
    fun getTimerTextLiveData(): LiveData<String> {
        return timerText
    }

    /**
     * Метод для получения инстанса LiveData с состоянием таймера.
     * Состояние таймера представлено порядковым номером константы перечисления [TimerState].
     *
     * @return LiveData с состоянием таймера.
     */
    fun getTimerStateLiveData(): LiveData<Int> {
        return timerState
    }
}
