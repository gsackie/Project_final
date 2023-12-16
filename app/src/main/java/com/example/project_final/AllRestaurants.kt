import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_final.R
import com.example.project_final.RestaurantScreen
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class AllRestaurants : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_restaurants, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewAllRestaurants)
        firestore = FirebaseFirestore.getInstance()
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        val adapter = RestaurantAdapter(emptyList()) { selectedRestaurant ->
            // Handle click on a restaurant card, navigate to the Restaurant screen
            val intent = Intent(requireContext(), RestaurantScreen::class.java)
            intent.putExtra("restaurant", selectedRestaurant)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Retrieve restaurants from Firestore
        val restaurantsCollection = firestore.collection("restaurants")

        restaurantsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val restaurantList = mutableListOf<Restaurant>()
                for (document in querySnapshot.documents) {
                    val name = document.getString("name") ?: ""
                    val location = document.getString("location") ?: ""
                    val imageUrl1 = document.getString("image1") ?: ""
                    val imageUrl2 = document.getString("image2") ?: ""
                    val imageUrl3 = document.getString("image3") ?: ""

                    // Handle null location
                    val restaurantLocation =
                        if (location.isNotEmpty()) location else "Default Location"

                    // Use geocoder to get the actual distance of the Restaurant from the user
                    val distance = calculateDistanceFromUser(restaurantLocation)

                    // Check if the restaurant is within 50 miles
                    if (distance <= 50) {
                        val restaurant = Restaurant(
                            name,
                            restaurantLocation,
                            listOf(imageUrl1, imageUrl2, imageUrl3)
                        )
                        restaurantList.add(restaurant)
                    }
                }

                // Update the data in the adapter
                adapter.updateData(restaurantList)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: $exception")
            }
    }

    private fun calculateDistanceFromUser(restaurantLocation: String): Float {

        // Example:
        val userLatitude = 37.7749
        val userLongitude = -122.4194

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocationName(restaurantLocation, 1)

        if (addresses.isNotEmpty()) {
            val restaurantLatitude = addresses[0].latitude
            val restaurantLongitude = addresses[0].longitude

            // Calculate distance
            val results = FloatArray(1)
            Location.distanceBetween(
                userLatitude,
                userLongitude,
                restaurantLatitude,
                restaurantLongitude,
                results
            )

            // Distance in miles
            return results[0] / 1609.344.toFloat()
        }

        // Return a large distance if geocoding fails
        return Float.MAX_VALUE
    }
}