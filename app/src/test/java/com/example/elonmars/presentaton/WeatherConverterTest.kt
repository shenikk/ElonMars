package com.example.elonmars.presentaton

import com.example.elonmars.presentation.weather.converter.WeatherConverter
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class Parametrized(
    private var value: String,
    private var expectedValue: String
) {

    private val weatherConverter = WeatherConverter()

    @Test
    fun convertToFahrenheitFailTest() {
        val result = weatherConverter.convertToFahrenheit(value)
        Assert.assertEquals(expectedValue, result)
    }

    companion object {
        @Parameterized.Parameters
        @JvmStatic
        fun parameters() = listOf(
            arrayOf("37", "98"),
            arrayOf("-98", "-144"),
            arrayOf("23.3", "73"),
            arrayOf("-23.3", "-9")
        )
    }
}
