// OrderItemListAdapter.kt
package com.example.project_final

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class OrderItemListAdapter(private val context: Context, private val orderItems: List<FoodItem>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return orderItems.size
    }

    override fun getItem(position: Int): Any {
        return orderItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val orderItem = getItem(position) as OrderItem
        viewHolder.foodNameTextView.text = orderItem.foodName
        viewHolder.priceTextView.text = "${orderItem.price}"
        viewHolder.quantityTextView.text = "Quantity: ${orderItem.quantity}"
        viewHolder.restaurantNameTextView.text = orderItem.restaurantName

        return view
    }

    private class ViewHolder(view: View) {
        val foodNameTextView: TextView = view.findViewById(R.id.foodNameTextView)
        val priceTextView: TextView = view.findViewById(R.id.priceTextView)
        val quantityTextView: TextView = view.findViewById(R.id.quantityTextView)
        val restaurantNameTextView: TextView = view.findViewById(R.id.restaurantNameTextView)
    }
}
