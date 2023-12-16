// CheckoutScreen.kt
package com.example.project_final

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Math.asin
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import kotlin.math.pow
import kotlin.random.Random

class CheckoutScreen : AppCompatActivity() {

    private lateinit var orderListView: ListView
    private lateinit var deliveryAddressEditText: EditText
    private lateinit var specialInstructionsEditText: EditText
    private lateinit var modifyOrderButton: Button
    private lateinit var placeOrderButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_screen)

        // Initialize views
        orderListView = findViewById(R.id.orderListView)
        deliveryAddressEditText = findViewById(R.id.deliveryAddressEditText)
        specialInstructionsEditText = findViewById(R.id.specialInstructionsEditText)
        modifyOrderButton = findViewById(R.id.modifyOrderButton)
        placeOrderButton = findViewById(R.id.placeOrderButton)

        // Set up the list of order items with adapter
        val orderItems = getOrderItems() // Implement getOrderItems() method
        val orderListAdapter = OrderItemListAdapter(this, orderItems)
        orderListView.adapter = orderListAdapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Not used for swipe gestures
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Implement deletion logic here
                val position = viewHolder.adapterPosition
                orderItems.removeAt(position)
                orderListAdapter.notifyDataSetChanged()
            }
        })

        itemTouchHelper.attachToRecyclerView(orderListView)


        // Set onClickListener for modify order button
        modifyOrderButton.setOnClickListener {
            // Implement modification logic (navigate back or modify the order)
        }

        // Set onClickListener for place order button
        placeOrderButton.setOnClickListener {
            placeOrder() // Implement placeOrder() method
        }
    }

    // Method to get the list of order items
    private fun getOrderItems() {
        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()

        // Reference to the "orderItems" collection
        val orderItemsCollection = db.collection("orderItems")

        orderItemsCollection.get()
            .addOnSuccessListener { result: QuerySnapshot ->
                val foodItems = mutableListOf<FoodItem>()

                for (document in result) {
                    val foodName = document.getString("foodName") ?: ""
                    val price = document.getDouble("price") ?: 0.0
                    val quantity = document.getLong("quantity")?.toInt() ?: 0
                    val restaurantName = document.getString("restaurantName") ?: ""

                    // Create a FoodItem object and add it to the list
                    val foodItem = FoodItem(foodName, price, quantity, restaurantName)
                    foodItems.add(foodItem)
                }

                // Now, you have the list of order items in the 'foodItems' variable
                // You can update your UI or perform further actions with this data
            }
            .addOnFailureListener { exception ->
                // Handle errors
                println("Error getting documents: $exception")
            }
    }

    // Method to place the order
    private fun placeOrder() {
        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()

        // Get order items
        val orderItems = getOrderItems()

        // Calculate distance
        val distance = calculateDistance("Customer Address", "Restaurant Address")

        // Calculate delivery time using the provided formula
        val deliveryTime = (distance / 100) * Random.nextInt(5, 101)

        // Create a new order document in Firestore
        val orderData = hashMapOf(
            "orderItems" to orderItems.map { item ->
                mapOf(
                    "foodName" to item.foodName,
                    "price" to item.price,
                    "quantity" to item.quantity,
                    "restaurantName" to item.restaurantName
                )
            },
            "deliveryAddress" to deliveryAddressEditText.text.toString(),
            "specialInstructions" to specialInstructionsEditText.text.toString(),
            "distance" to distance,
            "deliveryTime" to deliveryTime
        )

        // Add the order document to the "orders" collection
        db.collection("orders")
            .add(orderData)
            .addOnSuccessListener { documentReference ->
                // Order successfully placed, you can handle further actions here

                // Start the background service
                val serviceIntent = Intent(this, CheckoutBackgroundService::class.java)
                startService(serviceIntent)

                // Show a notification
                showNotification()

                // Navigate to the Orders screen after placing the order (replace this with your actual navigation logic)
                // Example: startActivity(Intent(this, OrdersActivity::class.java))
            }
            .addOnFailureListener { e ->
                // Handle errors
                println("Error placing order: $e")
            }
    }

    private fun calculateDistance{
        val userLatitude = 37.7749
        val userLongitude = -122.4194
        val restaurantLatitude = 37.7749
        val restaurantLongitude = -122.4194

        // Calculate distance using the Haversine formula
        val distance = 2 * 6371 * asin(
            sqrt(
                (sin((restaurantLatitude - userLatitude) / 2)).pow(2) +
                        cos(userLatitude) * cos(restaurantLatitude) * (sin((restaurantLongitude - userLongitude) / 2)).pow(
                    2
                )
            )
        )

        return distance
    }

    //method to generate notification
    private fun showNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a NotificationChannel for Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Order Placed")
            .setContentText("Your order has been placed successfully!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(1, builder.build())
    }


}
