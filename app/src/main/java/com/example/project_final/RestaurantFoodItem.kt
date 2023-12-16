package com.example.project_final

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView

import androidx.navigation.fragment.findNavController

class RestaurantFoodItem : Fragment() {

    private lateinit var foodListView: ListView
    private lateinit var checkoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_food_item, container, false)

        // Initialize views
        foodListView = view.findViewById(R.id.foodListView)
        checkoutButton = view.findViewById(R.id.checkoutButton)

        // Set up the list of food items with adapter
        val foodItems = getFoodItems()
        val foodListAdapter = FoodItemListAdapter(requireContext(), foodItems)
        foodListView.adapter = foodListAdapter

        // Set onClickListener for checkout button
        checkoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFoodItem_to_checkoutFragment)
        }

        return view
    }

    //method to get the list of food items
    private fun getFoodItems(): List<FoodItem> {

    }
}
