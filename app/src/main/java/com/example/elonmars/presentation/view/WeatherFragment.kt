package com.example.elonmars.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.MyApplication
import com.example.elonmars.R
import com.example.elonmars.databinding.FragmentWeatherBinding
import com.example.elonmars.di.activity.DaggerActivityComponent
import com.example.elonmars.presentation.adapter.WeatherAdapter
import com.example.elonmars.presentation.extensions.logError
import com.example.elonmars.presentation.extensions.showSnackbar
import com.example.elonmars.presentation.model.WeatherItem
import com.example.elonmars.presentation.viewmodel.WeatherViewModel
import io.reactivex.plugins.RxJavaPlugins

/** Экран с информацией о погоде за последние 10 доступных дней */
class WeatherFragment : Fragment() {

    private lateinit var weatherAdapter: WeatherAdapter

    private var viewModel: WeatherViewModel? = null
    private var dataSet: List<WeatherItem> = arrayListOf()

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provideDependencies(view.context)
        observeLiveData()
        if (savedInstanceState == null) {
            viewModel?.loadDataAsync()
        }
        initUI()
        setUpRecycler(binding.fragmentWeatherListContent.weatherRecycler)

        // Необходимо для устранения ошибки самого RxJava2 (UndeliverableException)
        RxJavaPlugins.setErrorHandler { throwable: Throwable? ->
            logError( "Exception: ${throwable.toString()}")
        }
        setUpToolBar(view)
    }

    override fun onResume() {
        super.onResume()
        binding.fragmentWeatherListContent.shimmerViewContainer.startShimmerAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        binding.swipeRefreshLayout.apply {
            this.setOnRefreshListener {
                viewModel?.loadDataOnForceAsync()
            }
        }

        binding.fragmentWeatherMainContent.temperatureSwitch.apply {
            this.isChecked = viewModel?.getFahrenheitEnabled() == true

            setOnCheckedChangeListener { _, isChecked ->
                viewModel?.setFahrenheitEnabled(isChecked)
                viewModel?.convertTemperature()
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
        binding.swipeRefreshLayout.isRefreshing = isRefreshing
    }

    private fun showError(throwable: Throwable) {
        logError("showError called with error = $throwable")
        showSnackbar(R.string.fragment_error_message)
    }

    private fun showProgress(isVisible: Boolean) {
        if (isVisible) {
            binding.fragmentWeatherListContent.shimmerViewContainer.startShimmerAnimation()
        } else {
            binding.fragmentWeatherListContent.shimmerViewContainer.stopShimmerAnimation()
            binding.fragmentWeatherListContent.shimmerViewContainer.visibility = View.GONE
        }
    }

    private fun showData(list: List<WeatherItem>) {
        weatherAdapter = WeatherAdapter(list)
        binding.fragmentWeatherListContent.weatherRecycler.adapter = weatherAdapter
    }

    private fun setLatestData(weatherItem: WeatherItem) {
        binding.fragmentWeatherMainContent.weatherDay.text = weatherItem.weatherDay
        binding.fragmentWeatherMainContent.tempHigh.text = weatherItem.highTemp
        binding.fragmentWeatherMainContent.tempLow.text = weatherItem.lowTemp
    }

    private fun setUpToolBar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}
