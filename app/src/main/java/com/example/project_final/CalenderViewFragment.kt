import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.project_final.MonthlyViewFragment
import com.example.project_final.R
import com.example.project_final.ViewPagerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var monthlyFragment: MonthlyViewFragment
    private lateinit var weeklyFragment: WeeklyViewFragment

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calender_view, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        calendarView = view.findViewById(R.id.materialCalendarView)

        monthlyFragment = MonthlyViewFragment()
        weeklyFragment = WeeklyViewFragment()

        setupViewPager()

        tabLayout.setupWithViewPager(viewPager)

        firestore = FirebaseFirestore.getInstance()
        setupCalendarView()

        return view
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(monthlyFragment, "Monthly View")
        adapter.addFragment(weeklyFragment, "Weekly View")
        viewPager.adapter = adapter
    }

    private fun setupCalendarView() {
        // Fetch all days information stored in the database
        val daysCollection = firestore.collection("days")

        daysCollection.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val date = document.getDate("date")
                    if (date != null) {
                        // Highlight the day on the calendar
                        monthlyFragment.highlightDate(date)

                        // Display the amount of dollars spent on that day
                        val dollarsSpent = document.getDouble("dollarsSpent")
                        if (dollarsSpent != null) {
                            val day = CalendarDay.from(date)
                            monthlyFragment.setDateText(day, "$$dollarsSpent")
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                println("Error getting documents: $exception")
            }
    }

    // Set a listener to handle date selection
    calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
        override fun onDateSelected(
            widget: MaterialCalendarView,
            date: CalendarDay,
            selected: Boolean
        ) {
            //show details for the selected date
            // navigate to a new screen or display information in a dialog
            // based on the selected date
            showDayDetails(date.date)
        }
    })
}

private fun showDayDetails(selectedDate: Date) {
    //Display a dialog with information about the selected date
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateString = dateFormat.format(selectedDate)

    val dialog = DayDetailsDialog.newInstance(dateString)
    dialog.show(requireFragmentManager(), DayDetailsDialog.TAG)
}
}
