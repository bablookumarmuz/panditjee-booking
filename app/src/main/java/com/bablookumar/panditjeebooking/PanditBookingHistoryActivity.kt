package com.bablookumar.panditjeebooking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.adapters.PanditBookingHistoryAdapter
import com.bablookumar.panditjeebooking.models.PanditBooking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PanditBookingHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pandit_booking_history)

        recyclerView = findViewById(R.id.recyclerBookingHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        loadBookingHistory()
    }

    private fun loadBookingHistory() {
        val uid = auth.currentUser?.uid ?: return
        val bookingsRef = database.child("pandit_bookings").child(uid)

        bookingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookings = mutableListOf<PanditBooking>()
                for (child in snapshot.children) {
                    val booking = child.getValue(PanditBooking::class.java)
                    booking?.let { bookings.add(it) }
                }
                recyclerView.adapter = PanditBookingHistoryAdapter(bookings)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
