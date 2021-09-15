package com.example.elonmars.data.provider

import android.util.Log
import com.example.elonmars.data.model.WeatherData
import com.example.elonmars.data.model.WeatherDataItem
import com.example.elonmars.data.WeatherApiInterface
import com.example.elonmars.data.exception.RequestException
import io.mockk.*
import org.junit.After
import org.junit.Assert
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

/** Класс для тестирования [WeatherItemsProvider] */
class WeatherItemsProviderTest {

    private val mockRetrofitClient: Retrofit = mockk()
    private val weatherItemsProvider = WeatherItemsProvider(mockRetrofitClient)
    private val weatherApi: WeatherApiInterface = mockk()
    private val mockCall: Call<WeatherData> = mockk()

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun loadWeatherItemsListTest() {
        // Arrange
        mockkStatic(Log::class)
        val data = WeatherData(arrayListOf(WeatherDataItem("1")))

        every { Log.e(any(), any()) } returns 0
        every { mockRetrofitClient.create(WeatherApiInterface::class.java) } returns weatherApi
        every { weatherApi.getWeatherData("weather", "msl", "json") } returns mockCall
        every { mockCall.execute() } returns Response.success(data)

        // Act
        val result = weatherItemsProvider.loadWeatherItemsList()

        // Assert
        verify(exactly = 1) { mockCall.execute() }
        Assert.assertEquals(WeatherDataItem("1"), result[0])
        Assert.assertEquals(1, result.size)
    }

    @Test(expected = RequestException::class)
    fun loadWeatherItemsListFailTest() {
        // Arrange
        mockkStatic(Log::class)
        val response = mockk<Response<WeatherData>>()

        every { Log.e(any(), any()) } returns 0
        every { mockRetrofitClient.create(WeatherApiInterface::class.java) } returns weatherApi
        every { weatherApi.getWeatherData("weather", "msl", "json") } returns mockCall
        every { response.code() } returns 404
        every { mockCall.execute() } throws RequestException("Response code: ${response.code()}")

        // Act
        weatherItemsProvider.loadWeatherItemsList()

        // Assert
        verify { Log.e(any(), any()) }
    }

    @Test
    fun loadWeatherItemsListFailBodyNullTest() {
        // Arrange
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { mockRetrofitClient.create(WeatherApiInterface::class.java) } returns weatherApi
        every { weatherApi.getWeatherData("weather", "msl", "json") } returns mockCall

        every { mockCall.execute() } throws RequestException("Body is null")

        try {
            // Act
            weatherItemsProvider.loadWeatherItemsList()
        } catch (e: Exception) {
            // Assert
            Assert.assertEquals("Body is null", e.message)
            verify(exactly = 0) { Log.e(any(), any()) }
        }
    }

    @Test(expected = Exception::class)
    fun loadWeatherItemsListFailBodyEmptyTest() {
        // Arrange
        mockkStatic(Log::class)
        val emptybody = WeatherData(arrayListOf())

        every { Log.e(any(), any()) } returns 0
        every { mockRetrofitClient.create(WeatherApiInterface::class.java) } returns weatherApi
        every { weatherApi.getWeatherData("weather", "msl", "json") } returns mockCall
        every { mockCall.execute() } returns Response.success(emptybody)

        // Act
        weatherItemsProvider.loadWeatherItemsList()
    }
}
