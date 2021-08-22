package com.example.elonmars

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setTheme()
    }

    private fun setTheme() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val nightMode = sharedPreferences.getBoolean("DarkTheme", false)
        if (nightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}