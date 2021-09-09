package com.example.elonmars.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.elonmars.R
import com.google.android.material.bottomnavigation.BottomNavigationView

/** Главное активити, которое является хостом для фрагметов. */
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//
//        bottomNavigationView.setupWithNavController(navController)
//
//        setUpNavigation(bottomNavigationView, navController)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        // Setup the ActionBar with navController and 5 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home_fragment, R.id.gallery_fragment,  R.id.mars_mission_fragment, R.id.rover_fragment, R.id.weather_fragment)
        )
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp(appBarConfiguration)
//    }

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

//    override fun onBackPressed() {
//        if (bottomNavigationView.selectedItemId == R.id.home) {
//            super.onBackPressed()
//        } else {
//            bottomNavigationView.selectedItemId = R.id.home
//        }
//    }


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
}
