package com.example.elonmars.presentation.viewmodel

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.R
import com.example.elonmars.domain.interactors.IHomeInteractor
import com.example.elonmars.presentation.enum.TimerState
import java.util.*
import java.util.concurrent.TimeUnit

class HomeViewModel(private val homeInteractor: IHomeInteractor) : ViewModel() {

    private val timerText = MutableLiveData<String>()
    private var timer: CountDownTimer? = null

    companion object {
        private const val COUNT_DOWN_INTERVAL = 1000L
    }

    fun startTimer(context: Context) {
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
                timerText.value =  context.getString(R.string.flight_message)
                homeInteractor.setTimerState(TimerState.NOT_STARTED)
            }
        }
        timer?.start()

        homeInteractor.setTimerState(TimerState.STARTED)
    }

    fun cancelTimer() {
        timer?.cancel()
    }

    fun getTimerState() = homeInteractor.getTimerState()

    fun setEndMillis(endMillis: Long) {
        homeInteractor.setEndMillis(endMillis)
    }

    fun getEndMillis() = homeInteractor.getEndMillis()

    fun getTimerTextLiveData(): LiveData<String> {
        return timerText
    }
}
