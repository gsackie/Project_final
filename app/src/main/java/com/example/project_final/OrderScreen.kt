package com.example.project_final

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class OrdersScreen : AppCompatActivity() {

    private lateinit var ordersListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_screen)

        // Initialize views
        ordersListView = findViewById(R.id.ordersListView)

        // Fetch and display orders
        fetchAndDisplayOrders()
    }

    private fun fetchAndDisplayOrders() {
        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()

        // Reference to the "orders" collection
        val ordersCollection = db.collection("orders")

        ordersCollection.get()
            .addOnSuccessListener { result ->
                val orderList = mutableListOf<Map<String, Any>>()

                for (document in result) {
                    val orderData = document.data
                    orderList.add(orderData)
                }

                // Create and set adapter for the ListView
                val ordersAdapter = OrdersAdapter(this, orderList)
                ordersListView.adapter = ordersAdapter

                // Set item click listener for the ListView
                ordersListView.setOnItemClickListener { _, _, position, _ ->
                    val selectedOrder = orderList[position]

                    // Start the Map activity and pass the order details
                    val intent = Intent(this, MapScreen::class.java)
                    intent.putExtra("selectedOrder", selectedOrder)
                    startActivity(intent)
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                println("Error getting documents: $exception")
            }
    }
}
