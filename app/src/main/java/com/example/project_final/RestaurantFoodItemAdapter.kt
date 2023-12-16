package com.example.project_final

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class FoodItemListAdapter(private val context: Context, private val foodItems: List<FoodItem>) : BaseAdapter() {

    override fun getCount(): Int = foodItems.size

    override fun getItem(position: Int): Any = foodItems[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.food_item_list_item, parent, false)

        val foodItem = getItem(position) as FoodItem

        // Set up views in the list item
        val itemNameTextView: TextView = view.findViewById(R.id.itemNameTextView)
        val itemPriceTextView: TextView = view.findViewById(R.id.itemPriceTextView)
        //Updating text view for quantity
        val itemQuantityTextView: TextView = view.findViewById(R.id.itemQuantityTextView)
        val addButton: Button = view.findViewById(R.id.addButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)

        itemNameTextView.text = foodItem.name
        itemPriceTextView.text = "$${foodItem.price}"
        itemQuantityTextView.text = foodItem.quantity.toString()

        // Set onClickListener for Add button
        addButton.setOnClickListener {
            foodItem.quantity++
            notifyDataSetChanged()
        }

        // Set onClickListener for Delete button
        deleteButton.setOnClickListener {
            if (foodItem.quantity > 0) {
                foodItem.quantity--
                notifyDataSetChanged()
            }
        }

        return view
    }
}
