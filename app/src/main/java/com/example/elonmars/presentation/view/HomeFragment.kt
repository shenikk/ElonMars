package com.example.elonmars.presentation.view

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.elonmars.MyApplication
import com.example.elonmars.R
import com.example.elonmars.databinding.FragmentHomeBinding
import com.example.elonmars.di.activity.DaggerActivityComponent
import com.example.elonmars.presentation.broadcast.NotificationBroadcastReceiver
import com.example.elonmars.presentation.enums.TimerState
import com.example.elonmars.presentation.service.NotificationService
import com.example.elonmars.presentation.viewmodel.HomeViewModel
import java.util.*

/** Главный экран приложения */
class HomeFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var calendar: Calendar
    private var notificationService: NotificationService? = null
    private var viewModel: HomeViewModel? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ENTER_FADE_TIME = 1000
        private const val EXIT_FADE_TIME = 3000
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provideDependencies(view.context)
        observeLiveData()
        initUI()

        startBackgroundAnimation(binding.root)
        notificationService = NotificationService()

        startTimerOnCreate(view.context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        binding.dateButton.apply {
            setOnClickListener {
                pickDate(this.context)
            }
        }
    }

    private fun provideDependencies(context: Context) {
        val appComponent = MyApplication.getAppComponent(context)
        val activityComponent = DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .build()

        viewModel = ViewModelProvider(this, activityComponent.getViewModelFactory()).get(
            HomeViewModel::class.java)
    }

    private fun observeLiveData() {
        viewModel?.let {
            it.getTimerTextLiveData().observe(viewLifecycleOwner, { text ->
                showData(text)
            })
            it.getTimerStateLiveData().observe(viewLifecycleOwner, { timerState ->
                setTextOnTimerFinish(timerState)
            })
        }
    }

    private fun showData(text: String) {
        binding.date.text = text
    }

    private fun setTextOnTimerFinish(timerState: Int) {
        if (timerState == TimerState.NOT_STARTED.ordinal) {
            binding.date.text = getString(R.string.flight_message)
        }
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
        viewModel?.cancelTimer()
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

        viewModel?.setEndMillis(calendar.timeInMillis)

        view?.context?.let { viewModel?.startTimer() }
        view?.context?.let { startAlarmManager(it) }
    }

    private fun startTimerOnCreate(context: Context) {
        if (viewModel?.getTimerState() == TimerState.STARTED.ordinal) {
            viewModel?.startTimer()
        }
    }

    private fun startAlarmManager(context: Context) {
        val notifyIntent = Intent(context, NotificationBroadcastReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notifyIntent,
            0
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewModel?.getEndMillis()?.let {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    it,
                    pendingIntent
                )
            }
        } else {
            viewModel?.getEndMillis()?.let {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    it,
                    pendingIntent
                )
            }
        }
    }
}
