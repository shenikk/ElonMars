package com.example.elonmars.presentation.model

/**
 * Модель для описания данных о погоде за конкретный день.
 * Применяется на уровне презентации.
 */
data class WeatherItem(
        var weatherDay: String?,
        var earthDate: String?,
        var highTemp: String?,
        var lowTemp: String?
)
