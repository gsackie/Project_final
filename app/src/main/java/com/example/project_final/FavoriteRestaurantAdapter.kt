package com.example.project_final

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firestore.admin.v1.Index

class FavoriteRestaurantAdapter(private val context: Context, private val restaurants: List<Restaurant>) :
    RecyclerView.Adapter<FavoriteRestaurantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_favorite_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]

        // Bind data to the view
        holder.restaurantName.text = restaurant.name

        // Set click listener to navigate to the Restaurant screen
        holder.itemView.setOnClickListener {
            val intent = Intent(context, RestaurantActivity::class.java)
            intent.putExtra("restaurantId", restaurant.restaurantId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantName: TextView = itemView.findViewById(R.id.restaurantNameTextView)

    }

}




