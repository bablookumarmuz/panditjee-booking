package com.bablookumar.panditjeebooking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameText: TextView
    private lateinit var emailText: TextView
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nameText = findViewById(R.id.profileName)
        emailText = findViewById(R.id.profileEmail)

        loadUserInfo()

        findViewById<Button>(R.id.btnBookingHistory).setOnClickListener {
            startActivity(Intent(this, BookingHistoryActivity::class.java))
        }

        findViewById<Button>(R.id.btnOrderHistory).setOnClickListener {
            startActivity(Intent(this, SamagriOrderHistoryActivity::class.java))
        }

        findViewById<Button>(R.id.btnEditProfile).setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        findViewById<Button>(R.id.btnNotificationSettings).setOnClickListener {
            startActivity(Intent(this, NotificationSettingsActivity::class.java))
        }

        findViewById<Button>(R.id.btnInvite).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Invite to PanditJee Booking")
            intent.putExtra(Intent.EXTRA_TEXT, "Join me on PanditJee Booking! Download from Play Store.")
            startActivity(Intent.createChooser(intent, "Invite via"))
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadUserInfo() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                nameText.text = doc.getString("name") ?: "User"
                emailText.text = doc.getString("email") ?: "Email not found"
            }
            .addOnFailureListener {
                nameText.text = "User"
                emailText.text = ""
            }
    }
}
