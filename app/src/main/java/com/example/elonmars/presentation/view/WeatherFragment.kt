package com.example.elonmars.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.elonmars.MyApplication
import com.example.elonmars.R
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.di.activity.DaggerActivityComponent
import com.example.elonmars.presentation.adapter.WeatherAdapter
import com.example.elonmars.presentation.extensions.logError
import com.example.elonmars.presentation.extensions.showSnackbar
import com.example.elonmars.presentation.model.WeatherItem
import com.example.elonmars.presentation.viewmodel.WeatherViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import io.reactivex.plugins.RxJavaPlugins

/** Экран с информацией о погоде за последние 10 доступных дней */
class WeatherFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var shimmerViewContainer: ShimmerFrameLayout
    private lateinit var weatherDay: TextView
    private lateinit var today: TextView
    private lateinit var highTemp: TextView
    private lateinit var lowTemp: TextView
    private lateinit var temperatureSwitch: SwitchCompat
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var dataStorage: IDataStorage

    private var viewModel: WeatherViewModel? = null
    private var dataSet: List<WeatherItem> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provideDependencies(view.context)
        observeLiveData()
        if (savedInstanceState == null) {
            viewModel?.loadDataAsync()
        }
        init()

        setUpRecycler(recyclerView)

        swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout).apply {
            this.setOnRefreshListener {
                viewModel?.loadDataOnForceAsync()
            }
        }

        // Необходимо для устранения ошибки самого RxJava2 (UndeliverableException)
        RxJavaPlugins.setErrorHandler { throwable: Throwable? ->
            logError( "Exception: ${throwable.toString()}")
        }
        setUpToolBar(view)
    }

    override fun onResume() {
        super.onResume()
        shimmerViewContainer.startShimmerAnimation()
    }

    private fun init() {
        view?.let {
            recyclerView = it.findViewById(R.id.weather_recycler)
            shimmerViewContainer = it.findViewById(R.id.shimmer_view_container)
            weatherDay = it.findViewById(R.id.weather_day)
            today = it.findViewById(R.id.today)
            highTemp = it.findViewById(R.id.temp_high)
            lowTemp = it.findViewById(R.id.temp_low)

            temperatureSwitch = it.findViewById<SwitchCompat>(R.id.temperature_switch).apply {
                this.isChecked = dataStorage.farenheitEnabled

                setOnCheckedChangeListener { _, isChecked ->
                    dataStorage.farenheitEnabled = isChecked
                    viewModel?.convertTemperature()
                }
            }
        }
    }

    private fun setUpRecycler(recyclerView: RecyclerView) {
        weatherAdapter = WeatherAdapter(dataSet)
        recyclerView.adapter = weatherAdapter
    }

    private fun provideDependencies(context: Context) {
        val appComponent = MyApplication.getAppComponent(context)
        val activityComponent = DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .build()

        viewModel = ViewModelProvider(this, activityComponent.getViewModelFactory()).get(
            WeatherViewModel::class.java)
        dataStorage = appComponent.getDataStorage()
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

            it.getRefreshLiveData().observe(viewLifecycleOwner, { isRefreshing ->
                showRefreshProgress(isRefreshing)
            })
        }
    }

    private fun showRefreshProgress(isRefreshing: Boolean) {
        swipeRefresh.isRefreshing = isRefreshing
    }

    private fun showError(throwable: Throwable) {
        logError("showError called with error = $throwable")
        showSnackbar(R.string.fragment_error_message)
    }

    private fun showProgress(isVisible: Boolean) {
        if (isVisible) {
            shimmerViewContainer.startShimmerAnimation()
        } else {
            shimmerViewContainer.stopShimmerAnimation()
            shimmerViewContainer.visibility = View.GONE
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

    private fun setUpToolBar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}
