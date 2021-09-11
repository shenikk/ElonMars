package com.example.elonmars.presentation.view

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.elonmars.MyApplication
import com.example.elonmars.R
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.presentation.broadcast.NotificationBroadcastReceiver
import com.example.elonmars.presentation.enum.TimerState
import com.example.elonmars.presentation.service.NotificationService
import java.util.*
import java.util.concurrent.TimeUnit

/** Главный экран приложения */
class HomeFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var dateText: TextView
    private lateinit var dataStorage: IDataStorage
    private var timer: CountDownTimer? = null
    private lateinit var calendar: Calendar
    private var notificationService: NotificationService? = null

    companion object {
        private const val ENTER_FADE_TIME = 1000
        private const val EXIT_FADE_TIME = 3000
        private const val COUNT_DOWN_INTERVAL = 1000L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layout = view.findViewById<RelativeLayout>(R.id.layout)
        startBackgroundAnimation(layout)
        notificationService = NotificationService()

        view.findViewById<Button>(R.id.date_button).apply {
            setOnClickListener {
                pickDate(this.context)
            }
        }
        dateText = view.findViewById(R.id.date)

        dataStorage = MyApplication.getAppComponent(view.context).getDataStorage()

        startTimerOnCreate()
    }

    private fun startAlarmManager(context: Context) {
        val notifyIntent = Intent(context, NotificationBroadcastReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            dataStorage.endMillis,
            pendingIntent
        )
    }

    private fun startBackgroundAnimation(view: View) {
        view.setBackgroundResource(R.drawable.gradient_list)

        val animationDrawable: AnimationDrawable = view.background as AnimationDrawable

        animationDrawable.apply {
            setEnterFadeDuration(ENTER_FADE_TIME)
            setExitFadeDuration(EXIT_FADE_TIME)
            start()
        }
    }

    private fun pickDate(context: Context) {
        timer?.cancel()
        DatePickerDialog(
            context,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        TimePickerDialog(
            this.context,
            this,
            Calendar.getInstance().get(Calendar.HOUR),
            Calendar.getInstance().get(Calendar.MINUTE),
            true
        ).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        dataStorage.endMillis = calendar.timeInMillis
        startTimer()
        view?.context?.let { startAlarmManager(it) }
    }

    private fun startTimerOnCreate() {
        if (dataStorage.timerState == TimerState.STARTED.ordinal) {
            startTimer()
        }
    }

    private fun startTimer() {
        val currentMillis = Calendar.getInstance().timeInMillis
        val endMillis = dataStorage.endMillis
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

                dateText.text = "$days days ${hours}h ${minutes}m ${seconds}s left"
            }

            override fun onFinish() {
                dateText.text = getString(R.string.flight_message)
                dataStorage.timerState = TimerState.NOT_STARTED.ordinal
            }
        }
        timer?.start()

        dataStorage.timerState = TimerState.STARTED.ordinal
    }

    //TODO that's just one more variant for animation
//    private fun startBackgroundAnimation(view: View) {
//        val animator = ValueAnimator.ofInt(Color.BLUE, Color.RED, Color.YELLOW, Color.MAGENTA)
//
//        animator.setEvaluator(ArgbEvaluator())
//        animator.repeatCount = ValueAnimator.INFINITE
//        animator.repeatMode = ValueAnimator.REVERSE
//        animator.duration = 10000
//        animator.interpolator = LinearInterpolator()
//
//        animator.addUpdateListener {
//            val value = it.animatedValue as Int
//
//            view.setBackgroundColor(value)
//        }
//        animator.start()
//    }
}
