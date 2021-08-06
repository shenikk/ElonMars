package com.example.elonmars.presentation.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.elonmars.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomSheet = findViewById<View>(R.id.bottom_sheet)
        val floatingButton = findViewById<FloatingActionButton>(R.id.floating_button)
        setupBottomSheet(bottomSheet, floatingButton)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        bottomNavigationView.setupWithNavController(navController)

        setUpNavigation(bottomNavigationView, navController)
    }

    private fun setUpNavigation(bottomNavigationView: BottomNavigationView, navController: NavController) {
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.home_fragment)
                    true
                }
                R.id.gallery -> {
                    navController.navigate(R.id.gallery_fragment)
                    true
                }
                R.id.rover -> {
                    navController.navigate(R.id.rover_fragment)
                    true
                }
                R.id.weather -> {
                    navController.navigate(R.id.weather_fragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupBottomSheet(bottomSheet: View, floatingButton: FloatingActionButton) {
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        floatingButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                floatingButton.animate().scaleX(1 - slideOffset)
                    .scaleY(1 - slideOffset)
                    .setDuration(0)
                    .start()
            }

        })
    }
}