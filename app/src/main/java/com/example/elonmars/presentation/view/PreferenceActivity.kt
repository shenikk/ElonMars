package com.example.elonmars.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.elonmars.R


class PreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)

        addFragment()

        PreferenceManager.setDefaultValues(this, R.xml.main_preference, true)
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_preference, MainPreferenceFragment(), MainPreferenceFragment.TAG)
            .commit()
    }
}