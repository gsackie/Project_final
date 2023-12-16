import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_final.R
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantAdapter(
    private var restaurants: List<Restaurant> = emptyList(),
    private val onItemClickListener: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(restaurant: Restaurant) {
            itemView.apply {
                // Bind data to views
                restaurantNameTextView.text = restaurant.name
                restaurantLocationTextView.text = restaurant.location

                // Load images using Glide
                for ((index, imageUrl) in restaurant.imageUrls.withIndex()) {
                    when (index) {
                        0 -> Glide.with(context).load(imageUrl).into(restaurantImageView1)
                        1 -> Glide.with(context).load(imageUrl).into(restaurantImageView2)
                        2 -> Glide.with(context).load(imageUrl).into(restaurantImageView3)
                        // Add more image views as needed
                    }
                }

                // Set click listener
                setOnClickListener { onItemClickListener.invoke(restaurant) }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount(): Int = restaurants.size

    // Update the data when needed
    fun updateData(newData: List<Restaurant>) {
        restaurants = newData
        notifyDataSetChanged()
    }

    companion object {
        private const val DEFAULT_IMAGE_URL = "https://example.com/default_image.jpg"
    }
}
