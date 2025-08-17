package com.bablookumar.panditjeebooking

import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PanditSettingsActivity : AppCompatActivity() {

    private lateinit var switchNotifications: Switch
    private lateinit var switchPreferences: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pandit_settings)

        switchNotifications = findViewById(R.id.switchNotifications)
        switchPreferences = findViewById(R.id.switchPreferences)

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, if (isChecked) "Notifications On" else "Notifications Off", Toast.LENGTH_SHORT).show()
        }

        switchPreferences.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, if (isChecked) "Preferences Enabled" else "Preferences Disabled", Toast.LENGTH_SHORT).show()
        }
    }
}
