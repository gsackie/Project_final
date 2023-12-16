package com.example.project_final

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore

class RestaurantImageFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var restaurantId: String
    private val images = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_image, container, false)
        viewPager = view.findViewById(R.id.viewPager)

        // Retrieve the restaurant ID from arguments
        restaurantId = arguments?.getString("restaurantId") ?: ""

        // Load images from Firestore for the given restaurant ID
        loadImages()

        return view
    }

    private fun loadImages() {
        val firestore = FirebaseFirestore.getInstance()
        val imagesCollection = firestore.collection("restaurants").document(restaurantId)
            .collection("images")

        imagesCollection.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val imageUrl = document.getString("imageUrl")
                if (imageUrl != null) {
                    images.add(imageUrl)
                }
            }

            // Set up the ViewPager with the loaded images
            viewPager.adapter = RestaurantImageAdapter(images)
        }.addOnFailureListener { exception ->
            // Handle the failure to retrieve images
        }
    }

    companion object {
        fun newInstance(restaurantId: String): RestaurantImageFragment {
            val fragment = RestaurantImageFragment()
            val args = Bundle()
            args.putString("restaurantId", restaurantId)
            fragment.arguments = args
            return fragment
        }
    }
}
