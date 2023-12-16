package com.example.project_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        // Setup navigation drawer header
        val headerView = navigationView.getHeaderView(0)
        val profileImageView: ImageView = headerView.findViewById(R.id.profileImageView)
        val userNameTextView: TextView = headerView.findViewById(R.id.userNameTextView)
        val emailTextView: TextView = headerView.findViewById(R.id.emailTextView)

        // Set user profile information
        //Get username and email from Firebase

        userNameTextView.text = "John Doe"
        emailTextView.text = "john.doe@example.com"


        // Set up navigation item click listener
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Handle Home button click
                    // code to navigate to the Home screen
                    val homeIntent = Intent(this, HomeScreen::class.java)
                    startActivity(homeIntent)
                    true
                }
                R.id.nav_recent_orders -> {
                    // Handle Recent Orders button click
                    //  code to navigate to the Recent Orders screen
                    val recentOrdersIntent = Intent(this, RecentOrdersFragment::class.java)
                    startActivity(recentOrdersIntent)
                    true
                }
                R.id.nav_sign_out -> {
                    // Handlling Sign Out button click
                    //End user session and navigate to Sign In screen
                    FirebaseAuth.getInstance().signOut()
                    val loginIntent = Intent(this, SignUpActivity::class.java)
                    startActivity(loginIntent)
                    finish() // Finish the MainActivity
                    true
                }
                else -> false
            }
        }
    }
}