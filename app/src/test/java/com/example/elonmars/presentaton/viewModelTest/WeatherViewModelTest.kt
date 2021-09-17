package com.example.elonmars.presentaton.viewModelTest

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.elonmars.data.exception.RequestException
import com.example.elonmars.domain.provider.ISchedulersProvider
import com.example.elonmars.domain.interactors.IWeatherInteractor
import com.example.elonmars.presentation.weather.model.WeatherItem
import com.example.elonmars.presentation.weather.viewmodel.WeatherViewModel
import io.mockk.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** Класс для тестирования [WeatherViewModel] */
class WeatherViewModelTest {

    @get:Rule
    var mRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var weatherViewModel: WeatherViewModel

    private var shimmerLiveDataObserver: Observer<Boolean> = mockk()
    private var refreshLiveDataObserver: Observer<Boolean> = mockk()
    private var errorLiveDataObserver: Observer<Throwable> = mockk()
    private var weatherItemsLiveDataObserver: Observer<List<WeatherItem>> = mockk()
    private var latestDayLiveDataObserver: Observer<WeatherItem> = mockk()

    private val schedulersProvider: ISchedulersProvider = mockk()
    private val weatherInteractor: IWeatherInteractor = mockk()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        weatherViewModel = WeatherViewModel(weatherInteractor, schedulersProvider)
        weatherViewModel.getShimmerLiveData().observeForever(shimmerLiveDataObserver)
        weatherViewModel.getRefreshLiveData().observeForever(refreshLiveDataObserver)
        weatherViewModel.getErrorLiveData().observeForever(errorLiveDataObserver)
        weatherViewModel.getWeatherItemsLiveData().observeForever(weatherItemsLiveDataObserver)
        weatherViewModel.getLatestDayLiveData().observeForever(latestDayLiveDataObserver)

        every { Log.e(any(), any()) } returns 0
        every { schedulersProvider.io() } returns Schedulers.trampoline()
        every { schedulersProvider.ui() } returns Schedulers.trampoline()

        every { shimmerLiveDataObserver.onChanged(any()) } just Runs
        every { refreshLiveDataObserver.onChanged(any()) } just Runs
        every { errorLiveDataObserver.onChanged(any()) } just Runs
        every { weatherItemsLiveDataObserver.onChanged(any()) } just Runs
        every { latestDayLiveDataObserver.onChanged(any()) } just Runs
    }

    @Test
    fun loadDataAsync() {
        // Arrange
        every { weatherInteractor.loadDataAsync() } returns Single.just(Unit)
        every { weatherInteractor.getWeatherItems() } returns celsiusWeatherItemList()
        every { weatherInteractor.getLatestWeatherDay() } returns celsiusWeatherItemList()[0]

        // Act
        weatherViewModel.loadDataAsync()

        // Assert
        verify(exactly = 1) { weatherInteractor.loadDataAsync() }
        verify(exactly = 1) { weatherInteractor.getWeatherItems() }
        verify(exactly = 1) { weatherInteractor.getLatestWeatherDay() }
        verify { errorLiveDataObserver wasNot called }
        verifyOrder {
            shimmerLiveDataObserver.onChanged(true)
            weatherItemsLiveDataObserver.onChanged(any())
            latestDayLiveDataObserver.onChanged(any())
            shimmerLiveDataObserver.onChanged(false)
        }
    }

    @Test
    fun loadDataOnForceAsync() {
        // Arrange
        every { weatherInteractor.loadDataAsyncOnCall() } returns Single.just(Unit)
        every { weatherInteractor.getWeatherItems() } returns celsiusWeatherItemList()
        every { weatherInteractor.getLatestWeatherDay() } returns celsiusWeatherItemList()[0]

        // Act
        weatherViewModel.loadDataOnForceAsync()

        // Assert
        verify(exactly = 1) { weatherInteractor.loadDataAsyncOnCall() }
        verify(exactly = 1) { weatherInteractor.getWeatherItems() }
        verify(exactly = 1) { weatherInteractor.getLatestWeatherDay() }
        verify { errorLiveDataObserver wasNot called }
        verifyOrder {
            refreshLiveDataObserver.onChanged(any())
            weatherItemsLiveDataObserver.onChanged(any())
            latestDayLiveDataObserver.onChanged(any())
            refreshLiveDataObserver.onChanged(any())
        }
    }

    @Test
    fun loadDataAsyncFail() {
        // Arrange
        val exception = RequestException("It's a test")
        every { weatherInteractor.loadDataAsync() } returns Single.error(exception)

        // Act
        weatherViewModel.loadDataAsync()

        // Assert
        verify(exactly = 0) { weatherItemsLiveDataObserver.onChanged(any()) }
        verify(exactly = 0) { latestDayLiveDataObserver.onChanged(any()) }
        verify(exactly = 1) { errorLiveDataObserver.onChanged(any()) }

        verifyOrder {
            shimmerLiveDataObserver.onChanged(true)
            errorLiveDataObserver.onChanged(exception)
            shimmerLiveDataObserver.onChanged(false)
        }
    }

    @Test
    fun loadDataAsyncDataEmpty() {
        // Arrange
        every { weatherInteractor.loadDataAsync() } returns Single.just(Unit)
        every { weatherInteractor.getWeatherItems() } returns listOf()
        every { weatherInteractor.getLatestWeatherDay() } returns null

        // Act
        weatherViewModel.loadDataAsync()

        // Assert
        verify(exactly = 1) { weatherItemsLiveDataObserver.onChanged(any()) }
        verify(exactly = 1) { latestDayLiveDataObserver.onChanged(any()) }
        verify(exactly = 0) { errorLiveDataObserver.onChanged(any()) }
    }

    @Test
    fun convertTemperatureTest() {
        // Arrange
        every { weatherInteractor.loadDataAsync() } returns Single.just(Unit)
        every { weatherInteractor.convertTemperature() } just Runs
        every { weatherInteractor.getWeatherItems() } returns celsiusWeatherItemList()
        every { weatherInteractor.getLatestWeatherDay() } returns celsiusWeatherItemList()[0]
        weatherViewModel.loadDataAsync()

        // Act
        weatherViewModel.convertTemperature()

        // Assert
        verifyOrder {
            weatherItemsLiveDataObserver.onChanged(any())
            latestDayLiveDataObserver.onChanged(any())
        }
    }

    @Test
    fun convertTemperatureDataEmpty() {
        // Arrange
        every { weatherInteractor.convertTemperature() } just Runs
        every { weatherInteractor.getWeatherItems() } returns null
        every { weatherInteractor.getLatestWeatherDay() } returns null

        // Act
        weatherViewModel.convertTemperature()

        // Assert
        verify(exactly = 1) { weatherItemsLiveDataObserver.onChanged(any()) }
        verify(exactly = 1) { latestDayLiveDataObserver.onChanged(any()) }
    }

    @Test
    fun getFahrenheitEnabledTest() {
        every { weatherInteractor.getFahrenheitEnabled() } returns true

        val result = weatherViewModel.getFahrenheitEnabled()

        verify(exactly = 1) { weatherInteractor.getFahrenheitEnabled() }
        Assert.assertEquals(true, result)
    }

    @Test
    fun setFahrenheitEnabledTest() {
        every { weatherInteractor.setFahrenheitEnabled(false) } just Runs

        weatherViewModel.setFahrenheitEnabled(false)

        verify(exactly = 1) { weatherInteractor.setFahrenheitEnabled(false) }
    }

    private fun celsiusWeatherItemList(): List<WeatherItem> {
        val list = arrayListOf<WeatherItem>()
        list.add(WeatherItem("Sol 1", "1", "High: 23 °C", "Low: 45 °C"))
        list.add(WeatherItem("Sol 2", "2", "High: 45 °C", "Low: -67 °C"))
        list.add(WeatherItem("Sol 3", "3", "High: 3 °C", "Low: -5 °C"))

        return list
    }
}
