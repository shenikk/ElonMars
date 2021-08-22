package com.example.elonmars.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.elonmars.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val bottomSheet = findViewById<View>(R.id.bottom_sheet)
//        val floatingButton = findViewById<FloatingActionButton>(R.id.floating_button)
//        setupBottomSheet(bottomSheet, floatingButton)


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
                R.id.mars_mission -> {
                    navController.navigate(R.id.mars_mission_fragment)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            goToPreferenceActivity()
        }
        return true
    }

    private fun goToPreferenceActivity() {
        val intent = Intent(this, PreferenceActivity::class.java)
        startActivity(intent)
    }
//
//    private fun setupBottomSheet(bottomSheet: View, floatingButton: FloatingActionButton) {
//        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
//
//        floatingButton.setOnClickListener {
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        }
//
//        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                floatingButton.animate().scaleX(1 - slideOffset)
//                    .scaleY(1 - slideOffset)
//                    .setDuration(0)
//                    .start()
//            }
//
//        })
//    }
}