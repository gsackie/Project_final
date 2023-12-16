// WeeklyViewFragment.kt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_weekly_view.*
import java.util.*

class WeeklyViewFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weekly_view, container, false)
        barChart = view.findViewById(R.id.barChart)
        firestore = FirebaseFirestore.getInstance()
        setupBarChart()
        return view
    }

    private fun setupBarChart() {
        // Fetch weekly spending data from Firestore
        val weeklySpendingCollection = firestore.collection("weekly_spending")

        weeklySpendingCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val entries = ArrayList<BarEntry>()

                for (document in querySnapshot.documents) {
                    val weeklySpending = document.toObject<WeeklySpending>()
                    if (weeklySpending != null) {
                        val dayOfWeek = weeklySpending.dayOfWeek.toFloat()
                        val moneySpent = weeklySpending.moneySpent.toFloat()
                        entries.add(BarEntry(dayOfWeek, moneySpent))
                    }
                }

                val dataSet = BarDataSet(entries, "Weekly Spending")

                val xAxis = barChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.labelCount = entries.size
                xAxis.setDrawGridLines(false)

                val barData = BarData(dataSet)
                barChart.data = barData
                barChart.invalidate()

                // Set a listener to handle bar click
                barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        // Show a popup window with the description of total money spent
                        // and the name of the restaurant from which the order was placed
                        val dayOfWeek = e?.x?.toInt() ?: 1
                        showBarDetails(dayOfWeek)
                    }

                    override fun onNothingSelected() {
                        // Handle when nothing is selected
                    }
                })
            }
            .addOnFailureListener { exception ->
                // Handle errors
                println("Error getting documents: $exception")
            }
    }

    // WeeklyViewFragment.kt
    private fun showBarDetails(dayOfWeek: Int) {
        // Fetch additional details from Firestore based on the selected dayOfWeek
        val weeklyDetailsCollection = firestore.collection("weekly_details")
            .whereEqualTo("dayOfWeek", dayOfWeek)

        weeklyDetailsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val document = querySnapshot.documents[0] // Assuming there is only one document per day
                    val totalMoneySpent = document.getDouble("totalMoneySpent")
                    val restaurantName = document.getString("restaurantName")

                    // Display details in a dialog or navigate to a new screen
                    println("Selected day of the week: $dayOfWeek")
                    println("Total money spent: $totalMoneySpent")
                    println("Restaurant name: $restaurantName")
                } else {
                    // Handle the case where no document is found for the selected dayOfWeek
                    println("No details found for day of the week: $dayOfWeek")
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                println("Error getting documents: $exception")
            }
    }
}