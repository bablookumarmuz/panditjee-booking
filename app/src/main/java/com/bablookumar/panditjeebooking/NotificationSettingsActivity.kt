package com.bablookumar.panditjeebooking

import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NotificationSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)

        val switchBooking = findViewById<Switch>(R.id.switchBookingNotifications)
        val switchOffers = findViewById<Switch>(R.id.switchOffersNotifications)

        switchBooking.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Booking notifications ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }

        switchOffers.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Offers notifications ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }
    }
}
