package com.bablookumar.panditjeebooking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.adapters.PanditBookingAdapter
import com.bablookumar.panditjeebooking.models.PanditBooking
import com.bablookumar.panditjeebooking.pandit.PanditProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PanditHomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var bookingsRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pandit_home)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        bookingsRecycler = findViewById(R.id.recyclerBookings)
        bookingsRecycler.layoutManager = LinearLayoutManager(this)

        loadBookings()

        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> true
                R.id.menu_samagri -> {
                    startActivity(Intent(this, SamagriActivity::class.java))
                    true
                }
                R.id.menu_profile -> {
                    startActivity(Intent(this, PanditProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadBookings() {
        val uid = auth.currentUser?.uid ?: return
        val bookingsRef = database.child("pandit_bookings").child(uid)

        bookingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookingList = mutableListOf<PanditBooking>()
                for (child in snapshot.children) {
                    val booking = child.getValue(PanditBooking::class.java)
                    booking?.let { bookingList.add(it) }
                }

                bookingsRecycler.adapter = PanditBookingAdapter(
                    bookingList
                ) { booking, action ->
                    // Accept / Decline handling
                    database.child("pandit_bookings")
                        .child(uid)
                        .child(booking.id)
                        .child("status")
                        .setValue(action)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
