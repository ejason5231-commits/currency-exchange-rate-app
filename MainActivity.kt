package com.example.currencyexchange

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_exchange -> loadFragment(ExchangeFragment())
                R.id.nav_gold -> loadFragment(GoldFragment())
                R.id.nav_calculate -> loadFragment(CalculateFragment())
            }
            true
        }
        // Load first screen
        loadFragment(ExchangeFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit()
    }
}
