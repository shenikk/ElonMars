package com.example.elonmars.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R
import com.example.elonmars.data.provider.SchedulersProvider
import com.example.elonmars.data.repository.ItemsRepository
import com.example.elonmars.presentation.adapter.WeatherAdapter
import com.example.elonmars.presentation.model.WeatherItem
import com.example.elonmars.presentation.viewmodel.WeatherViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import io.reactivex.plugins.RxJavaPlugins

/** Экран с информацией о погоде за последние 10 доступных дней */
class WeatherFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var mShimmerViewContainer: ShimmerFrameLayout
    private lateinit var weatherDay: TextView
    private lateinit var title: TextView
    private lateinit var today: TextView
    private lateinit var highTemp: TextView
    private lateinit var lowTemp: TextView
    private lateinit var temperatureSwitch: SwitchCompat

    private var viewModel: WeatherViewModel? = null
    private var dataSet: List<WeatherItem> = arrayListOf()

    private val TAG = "WeatherFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createViewModel()
        observeLiveData()
        if (savedInstanceState == null) {
            viewModel?.loadDataAsync()
        }
        init()

        setUpRecycler(recyclerView)

        // Необходимо для устранения ошибки самого RxJava2 (UndeliverableException)
        RxJavaPlugins.setErrorHandler { throwable: Throwable? ->
            Log.e(TAG, "Exception: ${throwable.toString()}")
        }
    }

    override fun onResume() {
        super.onResume()
        mShimmerViewContainer.startShimmerAnimation()
    }

    private fun init() {
        view?.let {
            recyclerView = it.findViewById(R.id.weather_recycler)
            mShimmerViewContainer = it.findViewById(R.id.shimmer_view_container)
            weatherDay = it.findViewById(R.id.weather_day)
            title = it.findViewById(R.id.title)
            today = it.findViewById(R.id.today)
            highTemp = it.findViewById(R.id.temp_high)
            lowTemp = it.findViewById(R.id.temp_low)
            temperatureSwitch = it.findViewById<SwitchCompat>(R.id.temperature_switch).apply {
                setOnClickListener {
                    viewModel?.convertTemperature()
                }
            }
        }
    }

    private fun setUpRecycler(recyclerView: RecyclerView) {
        weatherAdapter = WeatherAdapter(dataSet)
        recyclerView.adapter = weatherAdapter
    }

    private fun createViewModel() {
//        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {

                // Все зависимости уйдут после внедрения DI
                val itemsRepository = ItemsRepository()
                val schedulersProvider = SchedulersProvider()

                return WeatherViewModel(itemsRepository, schedulersProvider) as T
            }
        }).get(WeatherViewModel::class.java)
    }

    private fun observeLiveData() {
        viewModel?.let {
            it.getWeatherItemsLiveData().observe(viewLifecycleOwner, { list ->
                if (list != null) {
                    showData(list)
                }
            })

            it.getShimmerLiveData().observe(viewLifecycleOwner, { t ->
                if (t != null) {
                    showProgress(t)
                }
            })

            it.getErrorLiveData().observe(viewLifecycleOwner, { error ->
                showError(error)
            })

            it.getLatestDayLiveData().observe(viewLifecycleOwner, { weatherItem ->
                setLatestData(weatherItem)
            })
        }
    }

    private fun showError(throwable: Throwable) {
        Log.e(TAG, "showError called with error = $throwable")
        Snackbar.make(recyclerView, throwable.toString(), Snackbar.LENGTH_SHORT).show()
    }

    private fun showProgress(isVisible: Boolean) {
        if (isVisible) {
            mShimmerViewContainer.startShimmerAnimation()
        } else {
            mShimmerViewContainer.stopShimmerAnimation()
            mShimmerViewContainer.visibility = View.GONE
        }
    }

    private fun showData(list: List<WeatherItem>) {
        weatherAdapter = WeatherAdapter(list)
        recyclerView.adapter = weatherAdapter
    }

    private fun setLatestData(weatherItem: WeatherItem) {
        weatherDay.text = weatherItem.weatherDay
        highTemp.text = weatherItem.highTemp
        lowTemp.text = weatherItem.lowTemp
    }
}