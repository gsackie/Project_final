package com.example.project_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RestaurantScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_screen)

        //Load restaurant image fragment
        val fragment1 = RestaurantImageFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment1_container, fragment1)
            .commit()

    //Load restaurant info fragment
        val fragment2 = RestaurantFoodItem()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment2_container, fragment2)
            .commit()
    }


}