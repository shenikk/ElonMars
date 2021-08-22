package com.example.elonmars.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.elonmars.R

class MainPreferenceFragment : PreferenceFragmentCompat() {

    companion object {
        const val TAG = "MainPreferences"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)

        val switchPreferenceCompat =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.dark_theme_key))

        setPreferenceChangeListener(switchPreferenceCompat)
    }

    private fun setPreferenceChangeListener(
        switchPreferenceCompat: SwitchPreferenceCompat?
    ) {
        switchPreferenceCompat?.setOnPreferenceChangeListener { _, _ ->
            if (switchPreferenceCompat.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchPreferenceCompat.isChecked = false
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchPreferenceCompat.isChecked = true
            }
            true
        }
    }
}