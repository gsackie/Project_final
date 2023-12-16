package com.example.project_final

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query

class FavoriteRestaurantFragment : Fragment() {

    private lateinit var recentRestaurantsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_restaurant, container, false)

        recentRestaurantsRecyclerView = view.findViewById(R.id.FavoriteRestaurantsRecyclerView)

        // Retrieve recent restaurants data from Firestore
        db.collection("orders")
            .whereEqualTo("userId", "currentUserId") // Replace with the actual user ID
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val restaurantIds = querySnapshot.documents.map { it.getString("restaurantId") ?: "" }

                // Retrieve restaurant details based on the IDs
                db.collection("restaurants")
                    .whereIn("restaurantId", restaurantIds)
                    .get()
                    .addOnSuccessListener { restaurantSnapshot ->
                        val restaurants = restaurantSnapshot.toObjects(Restaurant::class.java)

                        // Set up RecyclerView with the adapter
                        val adapter = FavoriteRestaurantAdapter(requireContext(), restaurants)
                        recentRestaurantsRecyclerView.adapter = adapter
                    }
            }

        return view
    }
}
