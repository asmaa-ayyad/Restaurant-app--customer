package com.example.andro2groupprojecttest

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.andro2groupprojecttest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("login", MODE_PRIVATE)
        val isLoggedIn = sharedPref.contains("username") && sharedPref.contains("password")

        if (isLoggedIn) {
            navigateToMainContent()
        } else {
            navigateToLogin()
        }
    }

    private fun navigateToMainContent() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_search,R.id.navigation_profile,R.id.resturantFragment,R.id.userLocation
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun navigateToLogin() {
        val componentName = ComponentName(this, loginActivity::class.java)
        val loginIntent = Intent().setComponent(componentName)
        startActivity(loginIntent)
        finish()
    }

}
