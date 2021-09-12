package com.example.elonmars.presentation.viewmodel

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.R
import com.example.elonmars.domain.repositories.IHomeRepository
import com.example.elonmars.presentation.enum.TimerState
import java.util.*
import java.util.concurrent.TimeUnit

class HomeViewModel(private val homeRepository: IHomeRepository) : ViewModel() {

    private val timerText = MutableLiveData<String>()
    private var timer: CountDownTimer? = null

    companion object {
        private const val COUNT_DOWN_INTERVAL = 1000L
    }

    fun startTimer(context: Context) {
        val currentMillis = Calendar.getInstance().timeInMillis
        val endMillis = homeRepository.getEndMillis()
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
                homeRepository.setTimerState(TimerState.NOT_STARTED)
            }
        }
        timer?.start()

        homeRepository.setTimerState(TimerState.STARTED)
    }

    fun cancelTimer() {
        timer?.cancel()
    }

    fun getTimerState() = homeRepository.getTimerState()

    fun setEndMillis(endMillis: Long) {
        homeRepository.setEndMillis(endMillis)
    }

    fun getEndMillis() = homeRepository.getEndMillis()

    fun getTimerTextLiveData(): LiveData<String> {
        return timerText
    }
}
