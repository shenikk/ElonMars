package com.example.elonmars.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R
import com.example.elonmars.WeatherItem
import com.example.elonmars.presentation.adapter.WeatherAdapter
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

    private var viewModel: WeatherViewModel? = null
    private var dataSet: ArrayList<WeatherItem> = arrayListOf()

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

        recyclerView = view.findViewById(R.id.weather_recycler)
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container)
        weatherDay = view.findViewById(R.id.weather_day)
        title = view.findViewById(R.id.title)
        today = view.findViewById(R.id.today)
        highTemp = view.findViewById(R.id.temp_high)
        lowTemp = view.findViewById(R.id.temp_low)

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

    private fun setUpRecycler(recyclerView: RecyclerView) {
        weatherAdapter = WeatherAdapter(dataSet)
        recyclerView.adapter = weatherAdapter
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    private fun observeLiveData() {
        viewModel?.let {
            it.getWeatherItemsLiveData().observe(viewLifecycleOwner, { list ->
                if (list != null) {
                    showData(list)
                }
            })

            it.getProgressLiveData().observe(viewLifecycleOwner, { t ->
                if (t != null) {
                    showProgress(t)
                }
            })

            it.getErrorLiveData().observe(viewLifecycleOwner, { error ->
                showError(error)
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

    private fun showData(list: ArrayList<WeatherItem>) {
        prepareData(list)
        setTodayWeather()
        weatherAdapter = WeatherAdapter(dataSet)
        recyclerView.adapter = weatherAdapter
    }

    private fun prepareData(list: ArrayList<WeatherItem>) {
        list.take(10).forEach {
            dataSet.add(
                WeatherItem("Sol ${it.weatherDay}",
                    it.earthDate,
                    "High: ${it.highTemp} °C",
                    "Low: ${it.lowTemp} °C")
            )
        }
    }

    private fun setTodayWeather() {
        weatherDay.text = dataSet[0].weatherDay
        highTemp.text = dataSet[0].highTemp
        lowTemp.text = dataSet[0].lowTemp
    }
}