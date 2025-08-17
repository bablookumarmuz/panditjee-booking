package com.bablookumar.panditjeebooking.pandit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bablookumar.panditjeebooking.R
import com.google.firebase.auth.FirebaseAuth
import com.bablookumar.panditjeebooking.PanditBookingHistoryActivity
import com.bablookumar.panditjeebooking.PanditEditProfileActivity
import com.bablookumar.panditjeebooking.PanditSettingsActivity


class PanditProfileActivity : AppCompatActivity() {

    private lateinit var btnBookingHistory: Button
    private lateinit var btnEditProfile: Button
    private lateinit var btnSettings: Button
    private lateinit var btnLogout: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pandit_profile)

        auth = FirebaseAuth.getInstance()

        btnBookingHistory = findViewById(R.id.btnBookingHistory)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnSettings = findViewById(R.id.btnSettings)
        btnLogout = findViewById(R.id.btnLogout)

        // Open Booking History
        btnBookingHistory.setOnClickListener {
            startActivity(Intent(this, PanditBookingHistoryActivity::class.java))
        }

        // Open Edit Profile
        btnEditProfile.setOnClickListener {
            startActivity(Intent(this, PanditEditProfileActivity::class.java))
        }

        // Open Settings
        btnSettings.setOnClickListener {
            startActivity(Intent(this, PanditSettingsActivity::class.java))
        }

        // Logout
        btnLogout.setOnClickListener {
            auth.signOut()
            finish()
        }
    }
}
