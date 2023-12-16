package com.example.project_final

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_final.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RecentOrdersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recent_orders, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewRecentOrders)
        firestore = FirebaseFirestore.getInstance()
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        val adapter = RecentOrdersAdapter { selectedOrder ->
            // Handle click on "Place this order again" button
            placeOrderAgain(selectedOrder)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Retrieve recent orders from Firestore, sorted by date
        val ordersCollection = firestore.collection("orders")
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)

        ordersCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val orderList = mutableListOf<Order>()
                for (document in querySnapshot.documents) {
                    val orderId = document.id
                    val date = document.getDate("date") ?: Date()
                    val items = document.get("items") as? List<String> ?: emptyList()

                    val order = Order(orderId, date, items)
                    orderList.add(order)
                }

                // Update the data in the adapter
                adapter.updateData(orderList)
            }
            .addOnFailureListener { exception ->
                // Handle errors
                println("Error getting documents: $exception")
            }
    }

    private fun placeOrderAgain(selectedOrder: Order) {
        // Save the order again to the database in Firestore

        val ordersCollection = firestore.collection("orders")

        // Create a new order with the same items and the current date
        val newOrder = hashMapOf(
            "date" to Date(),
            "items" to selectedOrder.items
        )

        ordersCollection.add(newOrder)
            .addOnSuccessListener { documentReference ->
                val newOrderId = documentReference.id
            // Print a message or perform any additional actions after placing the order again

                // Update the recent restaurants in the Home screen
                val updateIntent = Intent("ACTION_UPDATE_RECENT_RESTAURANTS")
                updateIntent.putExtra("newOrderId", newOrderId)
                requireContext().sendBroadcast(updateIntent)

                // Take the user to the order screen where they can track their order
                val intent = Intent(requireContext(), OrdersScreen::class.java)
                intent.putExtra("orderId", newOrderId)
                startActivity(intent)
            }
            .addOnFailureListener { exception ->
                // Handle errors
                println("Error adding document: $exception")
            }
    }
}
