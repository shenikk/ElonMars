package com.example.elonmars.data.store

import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.model.PhotoItem

interface IDataStorage {
    var weatherDataItems: ArrayList<WeatherDataItem>?
    var photos: ArrayList<PhotoItem>?
    var farenheitEnabled: Boolean
}