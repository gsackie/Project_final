package com.example.project_final

data class Order(
    val foodItemName: String,
    val price: Double,
    val date: String,  // You may want to use a more appropriate date/time data type
    val time: String,
    val quantity: Int,
    val restaurantName: String,
    val deliveryAddress: String
)

