package com.example.elonmars.presentaton.viewModelTest

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.exception.RequestException
import com.example.elonmars.domain.provider.ISchedulersProvider
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.interactors.IWeatherInteractor
import com.example.elonmars.presentation.model.WeatherItem
import com.example.elonmars.presentation.viewmodel.WeatherViewModel
import io.mockk.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** Класс для тестирования [WeatherViewModel] */
class WeatherViewModelTest {

    @get:Rule
    var mRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var weatherViewModel: WeatherViewModel

    private var shimmerLiveDataObserver: Observer<Boolean> = mockk()
    private var errorLiveDataObserver: Observer<Throwable> = mockk()
    private var weatherItemsLiveDataObserver: Observer<List<WeatherItem>> = mockk()
    private var latestDayLiveDataObserver: Observer<WeatherItem> = mockk()

    private val schedulersProvider: ISchedulersProvider = mockk()
    private val weatherInteractor: IWeatherInteractor = mockk()
    private val dataStorage: IDataStorage = mockk()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        weatherViewModel = WeatherViewModel(weatherInteractor, schedulersProvider, dataStorage)
        weatherViewModel.getShimmerLiveData().observeForever(shimmerLiveDataObserver)
        weatherViewModel.getErrorLiveData().observeForever(errorLiveDataObserver)
        weatherViewModel.getWeatherItemsLiveData().observeForever(weatherItemsLiveDataObserver)
        weatherViewModel.getLatestDayLiveData().observeForever(latestDayLiveDataObserver)

        every { Log.e(any(), any()) } returns 0
        every { schedulersProvider.io() } returns Schedulers.trampoline()
        every { schedulersProvider.ui() } returns Schedulers.trampoline()

        every { shimmerLiveDataObserver.onChanged(any()) } just Runs
        every { errorLiveDataObserver.onChanged(any()) } just Runs
        every { weatherItemsLiveDataObserver.onChanged(any()) } just Runs
        every { latestDayLiveDataObserver.onChanged(any()) } just Runs
    }

    @Test
    fun loadDataAsync() {
        // Arrange
        every { weatherInteractor.loadDataAsync() } returns Single.just(createData())
        every { dataStorage.farenheitEnabled } returns true

        // Act
        weatherViewModel.loadDataAsync()

        // Assert
        verify(exactly = 1) { weatherInteractor.loadDataAsync() }
        verify { errorLiveDataObserver wasNot called }
        verifyOrder {
            shimmerLiveDataObserver.onChanged(true)
            weatherItemsLiveDataObserver.onChanged(any())
            latestDayLiveDataObserver.onChanged(any())
            shimmerLiveDataObserver.onChanged(false)
        }
    }

    @Test
    fun loadDataAsyncFarnheitEnabled() {
        // Arrange
        every { weatherInteractor.loadDataAsync() } returns Single.just(createData())
        every { dataStorage.farenheitEnabled } returns false

        // Act
        weatherViewModel.loadDataAsync()

        // Assert
        verify(exactly = 1) { weatherInteractor.loadDataAsync() }
        verify { errorLiveDataObserver wasNot called }
        verifyOrder {
            shimmerLiveDataObserver.onChanged(true)
            weatherItemsLiveDataObserver.onChanged(any())
            latestDayLiveDataObserver.onChanged(any())
            shimmerLiveDataObserver.onChanged(false)
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
        every { weatherInteractor.loadDataAsync() } returns Single.just(arrayListOf())

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
        every { weatherInteractor.loadDataAsync() } returns Single.just(createData())
        every { dataStorage.farenheitEnabled } returns false
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
        every { weatherInteractor.loadDataAsync() } returns Single.just(arrayListOf())
        weatherViewModel.loadDataAsync()

        // Act
        weatherViewModel.convertTemperature()

        // Assert
        verify(exactly = 1) { weatherItemsLiveDataObserver.onChanged(any()) }
        verify(exactly = 1) { latestDayLiveDataObserver.onChanged(any()) }
    }

    private fun createData(): ArrayList<WeatherDataItem> {
        val list = arrayListOf<WeatherDataItem>()
        list.add(WeatherDataItem("1", "1", "23", "45"))
        list.add(WeatherDataItem("2", "2", "45", "-67"))
        list.add(WeatherDataItem("3", "3", "3", "-5"))

        return list
    }
}