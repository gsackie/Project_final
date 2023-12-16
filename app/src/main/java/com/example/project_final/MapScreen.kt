import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project_final.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode

class MapScreen : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_screen)

        // Get the selected order details from the intent
        val selectedOrder = intent.getSerializableExtra("selectedOrder") as Map<String, Any>

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Using the selected order details to calculate the distance and draw the route on the map
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker for the destination location
        val destinationLatLng = LatLng(DESTINATION_LATITUDE, DESTINATION_LONGITUDE)
        mMap.addMarker(MarkerOptions().position(destinationLatLng).title("Destination"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(destinationLatLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))

        // Calculate distance and draw route on the map
        calculateAndDrawRoute(destinationLatLng)
    }

    private fun calculateAndDrawRoute(destinationLatLng: LatLng) {
        val context = GeoApiContext.Builder().apiKey("YOUR_GOOGLE_MAPS_API_KEY").build()

        val result: DirectionsResult = DirectionsApi.newRequest(context)
            .mode(TravelMode.DRIVING)
            .origin(LatLng(ORIGIN_LATITUDE, ORIGIN_LONGITUDE))
            .destination(destinationLatLng)
            .await()

        val points = result.routes[0].overviewPolyline.decodePath()

        mMap.addPolyline(
            PolylineOptions()
                .addAll(points)
                .color(resources.getColor(R.color.colorPrimary)) // Change the color as needed
        )
    }

    companion object {
        private const val DESTINATION_LATITUDE = 37.7749
        private const val DESTINATION_LONGITUDE = -122.4194


    }
}
