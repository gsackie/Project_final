package com.example.project_final

// MonthlyViewFragment.kt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_final.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.fragment_monthly_view.*
import java.text.SimpleDateFormat
import java.util.*

class MonthlyViewFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_monthly_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        setupCalendarView()
    }

    fun highlightDate(date: Date) {
        val day = CalendarDay.from(date)
        materialCalendarView.setDateSelected(day, true)
    }

    fun setDateText(day: CalendarDay, text: String) {
        materialCalendarView.setDateTextAppearance(day, R.style.CalendarDayText)
        materialCalendarView.setDateText(day, text)
    }

    private fun setupCalendarView() {
        // Set a listener to handle date selection
        materialCalendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
                // Show details for the selected date
                // You can navigate to a new screen or display information in a dialog
                // based on the selected date
                showDayDetails(date.date)
            }
        })

        // Fetch data from Firestore
        val daysCollection = firestore.collection("days")

        daysCollection.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val date = document.getDate("date")
                    if (date != null) {

                        highlightDate(date)


                        val dollarsSpent = document.getDouble("dollarsSpent")
                        if (dollarsSpent != null) {
                            val day = CalendarDay.from(date)
                            setDateText(day, "$$dollarsSpent")
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                println("Error getting documents: $exception")
            }
    }

    private fun showDayDetails(selectedDate: Date) {
        // Display a dialog with information about the selected date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = dateFormat.format(selectedDate)

        val dialog = DayDetailsDialog.newInstance(dateString)
        dialog.show(requireFragmentManager(), DayDetailsDialog.TAG)
    }
}
