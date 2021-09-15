package com.example.elonmars.data.repository

import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.exception.RequestException
import com.example.elonmars.domain.provider.IWeatherItemsProvider
import com.example.elonmars.data.store.IDataStorage
import io.mockk.*
import io.reactivex.observers.TestObserver
import org.junit.Test

/** Класс для тестирования [ItemsRepository] */
class ItemsRepositoryTest {

    private var firstWeatherDataItem = WeatherDataItem("1", "1", "12", "-212")
    private var secondWeatherDataItem = WeatherDataItem("2", "2", "52", "-12")
    private var thirdWeatherDataItem = WeatherDataItem("3", "3", "22", "-82")

    private var dataStorage: IDataStorage = mockk()
    private var weatherItemsProvider: IWeatherItemsProvider = mockk()
    private var itemsRepository = ItemsRepository(dataStorage, weatherItemsProvider)

    @Test
    fun loadDataAsyncCacheTest() {
        // Arrange
        every { dataStorage.weatherDataItems } returns arrayListOf(firstWeatherDataItem, secondWeatherDataItem, thirdWeatherDataItem)
        every { weatherItemsProvider.loadWeatherItemsList() } returns arrayListOf(firstWeatherDataItem)

        // Act
        val testObserver: TestObserver<List<WeatherDataItem>> = itemsRepository.loadDataAsync().test()

        // Assert
        testObserver.assertResult(arrayListOf(firstWeatherDataItem, secondWeatherDataItem, thirdWeatherDataItem))
        testObserver.assertValueAt(0, arrayListOf(firstWeatherDataItem, secondWeatherDataItem, thirdWeatherDataItem))
        testObserver.assertNever(arrayListOf(firstWeatherDataItem))
        testObserver.assertNoErrors()
        verify(exactly = 1) { dataStorage.weatherDataItems }
        verify(exactly = 0) { weatherItemsProvider.loadWeatherItemsList() }
        testObserver.dispose()
    }

    @Test
    fun loadDataAsyncNoCacheTest() {
        // Arrange
        every { dataStorage.weatherDataItems } returns null
        every { weatherItemsProvider.loadWeatherItemsList() } returns arrayListOf(firstWeatherDataItem)
        every { dataStorage.weatherDataItems = arrayListOf(firstWeatherDataItem) } just Runs

        // Act
        val testObserver: TestObserver<List<WeatherDataItem>> = itemsRepository.loadDataAsync().test()

        // Assert
        testObserver.assertResult(arrayListOf(firstWeatherDataItem))
        testObserver.assertValueAt(0, arrayListOf(firstWeatherDataItem))
        testObserver.assertNever(arrayListOf(firstWeatherDataItem, secondWeatherDataItem, thirdWeatherDataItem))
        testObserver.assertNoErrors()
        verify(exactly = 1) { dataStorage.weatherDataItems }
        verify(exactly = 1) { weatherItemsProvider.loadWeatherItemsList() }
        testObserver.dispose()
    }

    @Test
    fun loadDataAsyncEmptyListTest() {
        // Arrange
        every { weatherItemsProvider.loadWeatherItemsList() } returns arrayListOf(firstWeatherDataItem)
        every { dataStorage.weatherDataItems } returns arrayListOf()

        // Act
        val testObserver: TestObserver<List<WeatherDataItem>> = itemsRepository.loadDataAsync().test()

        // Assert
        testObserver.assertResult(arrayListOf())
        testObserver.assertNever(arrayListOf(firstWeatherDataItem))
        testObserver.assertNoErrors()
        verify(exactly = 1) { dataStorage.weatherDataItems }
        testObserver.dispose()
    }

    @Test
    fun loadDataAsyncFailTest() {
        // Arrange
        every { weatherItemsProvider.loadWeatherItemsList() } throws RequestException("Can't create Json, parsing is down")
        every { dataStorage.weatherDataItems } returns null

        // Act
        val testObserver: TestObserver<List<WeatherDataItem>> = itemsRepository.loadDataAsync().test()

        // Assert
        testObserver.assertNever(arrayListOf(firstWeatherDataItem))
        testObserver.assertFailureAndMessage(RequestException::class.java, "Can't create Json, parsing is down")
        verify(exactly = 1) { weatherItemsProvider.loadWeatherItemsList() }
        testObserver.dispose()
    }

    @Test
    fun loadDataAsyncOnCall() {
        // Arrange
        every { weatherItemsProvider.loadWeatherItemsList() } returns arrayListOf(firstWeatherDataItem, secondWeatherDataItem)
        every { dataStorage.weatherDataItems = arrayListOf(firstWeatherDataItem, secondWeatherDataItem) } just Runs

        // Act
        val testObserver: TestObserver<List<WeatherDataItem>> = itemsRepository.loadDataAsyncOnCall().test()

        // Assert
        testObserver.assertResult(arrayListOf(firstWeatherDataItem, secondWeatherDataItem))
        testObserver.assertNever(arrayListOf(firstWeatherDataItem))
        testObserver.assertNoErrors()

        verify(exactly = 0) { dataStorage.weatherDataItems }
        verify(exactly = 1) { dataStorage.weatherDataItems = arrayListOf(firstWeatherDataItem, secondWeatherDataItem) }
        verify(exactly = 1) { weatherItemsProvider.loadWeatherItemsList() }
        testObserver.dispose()
    }

    @Test
    fun loadDataAsyncOnCallEmptyList() {
        // Arrange
        every { weatherItemsProvider.loadWeatherItemsList() } returns arrayListOf()
        every { dataStorage.weatherDataItems = arrayListOf() } just Runs

        // Act
        val testObserver: TestObserver<List<WeatherDataItem>> = itemsRepository.loadDataAsyncOnCall().test()

        // Assert
        testObserver.assertResult(arrayListOf())
        testObserver.assertNever(arrayListOf(firstWeatherDataItem))
        testObserver.assertNoErrors()

        verify(exactly = 0) { dataStorage.weatherDataItems }
        verify(exactly = 1) { dataStorage.weatherDataItems = arrayListOf() }
        verify(exactly = 1) { weatherItemsProvider.loadWeatherItemsList() }
        testObserver.dispose()
    }

    @Test
    fun loadDataAsyncOnCallFail() {
        // Arrange
        every { weatherItemsProvider.loadWeatherItemsList() } throws RequestException("Return failed")
        every { dataStorage.weatherDataItems = arrayListOf() } just Runs

        // Act
        val testObserver: TestObserver<List<WeatherDataItem>> = itemsRepository.loadDataAsyncOnCall().test()

        // Assert
        testObserver.assertNever(arrayListOf(firstWeatherDataItem))
        testObserver.assertFailureAndMessage(RequestException::class.java, "Return failed")

        verify(exactly = 0) { dataStorage.weatherDataItems }
        verify(exactly = 0) { dataStorage.weatherDataItems = arrayListOf() }
        verify(exactly = 1) { weatherItemsProvider.loadWeatherItemsList() }
        testObserver.dispose()
    }
}