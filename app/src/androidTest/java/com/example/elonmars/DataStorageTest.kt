package com.example.elonmars

import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.data.model.WeatherDataItem
import com.example.elonmars.data.store.DataPreferences
import com.example.elonmars.presentation.model.WeatherItem
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/** Класс для проверки работы SharedPreferences, который используется в классе [DataStorage] */
class DataStorageTest : DataPreferences() {

    private lateinit var sharedPreferences: SharedPreferences
    private val prefsName = "prefs"

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    @Test
    fun weatherDataItemsTest() {
        val weatherDataItemList = createData()

        sharedPreferences.edit().putString("WEATHER_KEY", convertDataToJson(createData())).apply()
        val result: List<WeatherDataItem>? = sharedPreferences.getString("WEATHER_KEY", null)
            ?.let { getDataFromJson(it) }

        Assert.assertEquals(weatherDataItemList, result)
    }

    @Test
    fun weatherItemsTest() {
        val weatherItemList = fahrenheitWeatherItemList()

        sharedPreferences.edit().putString("WEATHER_ITEM_KEY", convertDataToJson(weatherItemList)).apply()
        val result: List<WeatherItem>? = sharedPreferences.getString("WEATHER_ITEM_KEY", null)
            ?.let { getDataFromJson(it) }

        Assert.assertEquals(weatherItemList, result)
    }

    @Test
    fun latestWeatherDayTest() {
        val latestWeatherDay = fahrenheitWeatherItemList()[0]

        sharedPreferences.edit().putString("LATEST_WEATHER_DAY_KEY", convertItemToJson(latestWeatherDay)).apply()
        val result: WeatherItem? = sharedPreferences.getString("LATEST_WEATHER_DAY_KEY", null)
            ?.let { getItemFromJson(it) }

        Assert.assertEquals(latestWeatherDay, result)
    }

    @Test
    fun photosTest() {
        val photos = getList()

        sharedPreferences.edit().putString("PHOTOS_KEY", convertDataToJson(photos)).apply()
        val result: List<PhotoItem>? = sharedPreferences.getString("PHOTOS_KEY", null)
            ?.let { getDataFromJson(it) }

        Assert.assertEquals(photos, result)
    }

    @Test
    fun favouritePhotosTest() {
        val photos = getList()

        sharedPreferences.edit().putString("FAVOURITE_PHOTOS_KEY", convertDataToJson(photos)).apply()
        val result: List<PhotoItem>? = sharedPreferences.getString("FAVOURITE_PHOTOS_KEY", null)
            ?.let { getDataFromJson(it) }

        Assert.assertEquals(photos, result)
    }

    @Test
    fun fahrenheitEnabledFalseTest() {
        val value = false

        sharedPreferences.edit().putBoolean("CONVERSION_KEY", value).apply()
        val result: Boolean = sharedPreferences.getBoolean("CONVERSION_KEY", false)

        Assert.assertEquals(value, result)
    }

    @Test
    fun fahrenheitEnabledTrueTest() {
        val value = true

        sharedPreferences.edit().putBoolean("CONVERSION_KEY", value).apply()
        val result: Boolean = sharedPreferences.getBoolean("CONVERSION_KEY", false)

        Assert.assertEquals(value, result)
    }

    @Test
    fun endMillisTest() {
        val value = 1000L

        sharedPreferences.edit().putLong("END_MILLIS_KEY", value).apply()
        val result: Long = sharedPreferences.getLong("END_MILLIS_KEY", 0)

        Assert.assertEquals(value, result)
    }

    @Test
    fun timerStateTest() {
        val value = 0

        sharedPreferences.edit().putInt("TIMER_STATE_KEY", value).apply()
        val result: Int = sharedPreferences.getInt("TIMER_STATE_KEY", 0)

        Assert.assertEquals(value, result)
    }

    @Test
    fun contentTypeTest() {
        val value = 1

        sharedPreferences.edit().putInt("TIMER_STATE_KEY", value).apply()
        val result: Int = sharedPreferences.getInt("TIMER_STATE_KEY", 0)

        Assert.assertEquals(value, result)
    }



    private fun createData(): List<WeatherDataItem> {
        val list = arrayListOf<WeatherDataItem>()
        list.add(WeatherDataItem("1", "1", "23", "45"))
        list.add(WeatherDataItem("2", "2", "45", "-67"))
        list.add(WeatherDataItem("3", "3", "3", "-5"))

        return list
    }

    private fun fahrenheitWeatherItemList(): List<WeatherItem> {
        val list = arrayListOf<WeatherItem>()
        list.add(WeatherItem("Sol 1", "1", "High: 73 °F", "Low: 113 °F"))
        list.add(WeatherItem("Sol 2", "2", "High: 113 °F", "Low: -88 °F"))
        list.add(WeatherItem("Sol 3", "3", "High: 37 °F", "Low: 23 °F"))

        return list
    }

    private fun getList(): List<PhotoItem> = listOf(
        PhotoItem("title1", mediaType = "image"),
        PhotoItem("title2", mediaType = "image"),
        PhotoItem("title3", mediaType = "image")
    )
}
