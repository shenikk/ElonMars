package com.example.elonmars.presentation.weather.converter

import com.example.elonmars.presentation.extensions.logError

/** Класс, отвечающий за конвертацию единиц измерения */
@Suppress("MagicNumber") // числа, используемые в методе необходимы для конвертации
class WeatherConverter {

    /**
     * Конвертирует градусы Цельсия в градусы Фаренгейт.
     *
     * @param temperature градусы Цельсия, представленные строкой.
     * @param temperature градусы Цельсия, представленные строкой.
     */
    fun convertToFahrenheit(temperature: String?): String? {
        return try {
            (temperature?.toFloat()?.let { (it * 9f / 5f) + 32f })?.toInt().toString()
        } catch (e: NumberFormatException) {
            logError("$temperature is not a number")
            null
        }
    }
}
