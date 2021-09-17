package com.example.elonmars.domain

import com.example.elonmars.data.model.WeatherDataItem
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.interactors.WeatherInteractor
import com.example.elonmars.domain.repositories.IItemsRepository
import com.example.elonmars.presentation.model.WeatherItem
import io.mockk.*
import io.reactivex.Single
import org.junit.Assert
import org.junit.Test

/** Класс для тестирования [WeatherInteractor] */
class WeatherInteractorTest {

    private val itemsRepository: IItemsRepository = mockk()
    private val dataStorage: IDataStorage = mockk()
    private val weatherInteractor = WeatherInteractor(itemsRepository, dataStorage)
    private val latestWeatherDay = WeatherItem("Sol 1", "1", "High: 23 °C", "Low: 45 °C")
    private val latestWeatherDayFahrenheit = WeatherItem("Sol 1", "1", "High: 73 °F", "Low: 113 °F")

    @Test
    fun loadDataAsyncWithCelsiusTest() {
        every { itemsRepository.loadDataAsync() } returns Single.just(createData())
        every { dataStorage.weatherItems = celsiusWeatherItemList() } just Runs
        every { dataStorage.latestWeatherDay = latestWeatherDay } just Runs
        every { dataStorage.fahrenheitEnabled } returns false

        val testObserver = weatherInteractor.loadDataAsync().test()

        verify(exactly = 1) { itemsRepository.loadDataAsync() }
        testObserver.assertResult(Unit)
    }

    @Test
    fun loadDataAsyncWithFahrenheitTest() {
        every { itemsRepository.loadDataAsync() } returns Single.just(createData())
        every { dataStorage.weatherItems = fahrenheitWeatherItemList() } just Runs
        every { dataStorage.latestWeatherDay = latestWeatherDayFahrenheit } just Runs
        every { dataStorage.fahrenheitEnabled } returns true

        val testObserver = weatherInteractor.loadDataAsync().test()

        verify(exactly = 1) { itemsRepository.loadDataAsync() }
        testObserver.assertResult(Unit)
    }

    @Test
    fun loadDataAsyncOnCallWithCelsiusTest() {
        every { itemsRepository.loadDataAsyncOnCall() } returns Single.just(createData())
        every { dataStorage.weatherItems = celsiusWeatherItemList() } just Runs
        every { dataStorage.latestWeatherDay = latestWeatherDay } just Runs
        every { dataStorage.fahrenheitEnabled } returns false

        val testObserver = weatherInteractor.loadDataAsyncOnCall().test()

        verify(exactly = 1) { itemsRepository.loadDataAsyncOnCall() }
        testObserver.assertResult(Unit)
    }

    @Test
    fun loadDataAsyncOnCallWithFahrenheitTest() {
        every { itemsRepository.loadDataAsyncOnCall() } returns Single.just(createData())
        every { dataStorage.weatherItems = fahrenheitWeatherItemList() } just Runs
        every { dataStorage.latestWeatherDay = latestWeatherDayFahrenheit } just Runs
        every { dataStorage.fahrenheitEnabled } returns true

        val testObserver = weatherInteractor.loadDataAsyncOnCall().test()

        verify(exactly = 1) { itemsRepository.loadDataAsyncOnCall() }
        testObserver.assertResult(Unit)
    }

    /** No method is invoked because 'weatherItemsData' is empty */
    @Test
    fun convertTemperatureTest() {
        every { dataStorage.fahrenheitEnabled } returns true
        every { dataStorage.weatherItems = fahrenheitWeatherItemList() } just Runs
        every { dataStorage.latestWeatherDay = latestWeatherDayFahrenheit } just Runs

        weatherInteractor.convertTemperature()

        verify(exactly = 0) { dataStorage.fahrenheitEnabled }
        verify(exactly = 0) { dataStorage.weatherItems = fahrenheitWeatherItemList() }
        verify(exactly = 0) { dataStorage.latestWeatherDay = latestWeatherDayFahrenheit }
    }

    @Test
    fun getLatestWeatherDayTest() {
        every { dataStorage.latestWeatherDay } returns latestWeatherDay

        val result = weatherInteractor.getLatestWeatherDay()

        Assert.assertEquals(latestWeatherDay, result)
    }

    @Test
    fun getWeatherItemsTest() {
        every { dataStorage.weatherItems } returns celsiusWeatherItemList()

        val result = weatherInteractor.getWeatherItems()

        verify(exactly = 1) { dataStorage.weatherItems }
        Assert.assertEquals(result?.size, result?.size)
        Assert.assertEquals(result?.get(0), celsiusWeatherItemList()[0])
        Assert.assertEquals(result?.get(1), celsiusWeatherItemList()[1])
        Assert.assertEquals(result?.get(2), celsiusWeatherItemList()[2])
    }

    @Test
    fun getWeatherItemsEmptyListTest() {
        every { dataStorage.weatherItems } returns listOf()

        val result = weatherInteractor.getWeatherItems()

        verify(exactly = 1) { dataStorage.weatherItems }
        Assert.assertEquals(listOf<WeatherItem>(), result)
        Assert.assertEquals(0, result?.size)
    }

    @Test
    fun getFahrenheitEnabledTest() {
        every { dataStorage.fahrenheitEnabled } returns true

        val result = weatherInteractor.getFahrenheitEnabled()

        verify(exactly = 1) { dataStorage.fahrenheitEnabled }
        Assert.assertEquals(true, result)
    }

    @Test
    fun setFahrenheitEnabledTest() {
        every { dataStorage.fahrenheitEnabled = false } just Runs

        weatherInteractor.setFahrenheitEnabled(false)

        verify(exactly = 1) { dataStorage.fahrenheitEnabled = false }
    }

    private fun createData(): List<WeatherDataItem> {
        val list = arrayListOf<WeatherDataItem>()
        list.add(WeatherDataItem("1", "1", "23", "45"))
        list.add(WeatherDataItem("2", "2", "45", "-67"))
        list.add(WeatherDataItem("3", "3", "3", "-5"))

        return list
    }

    private fun celsiusWeatherItemList(): List<WeatherItem> {
        val list = arrayListOf<WeatherItem>()
        list.add(WeatherItem("Sol 1", "1", "High: 23 °C", "Low: 45 °C"))
        list.add(WeatherItem("Sol 2", "2", "High: 45 °C", "Low: -67 °C"))
        list.add(WeatherItem("Sol 3", "3", "High: 3 °C", "Low: -5 °C"))

        return list
    }

    private fun fahrenheitWeatherItemList(): List<WeatherItem> {
        val list = arrayListOf<WeatherItem>()
        list.add(WeatherItem("Sol 1", "1", "High: 73 °F", "Low: 113 °F"))
        list.add(WeatherItem("Sol 2", "2", "High: 113 °F", "Low: -88 °F"))
        list.add(WeatherItem("Sol 3", "3", "High: 37 °F", "Low: 23 °F"))

        return list
    }
}
