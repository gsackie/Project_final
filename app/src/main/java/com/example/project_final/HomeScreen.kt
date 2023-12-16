package com.example.project_final

import AllRestaurants
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        // Load Fragment1
        val fragment1 = FavoriteRestaurantFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment1_container, fragment1)
            .commit()

        // Load Fragment2
        val fragment2 = AllRestaurants()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment2_container, fragment2)
            .commit()
    }
}