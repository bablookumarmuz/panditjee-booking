package com.bablookumar.panditjeebooking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.adapters.BookingAdapter
import com.bablookumar.panditjeebooking.models.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookingHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookingAdapter
    private val bookings = mutableListOf<Booking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)

        recyclerView = findViewById(R.id.bookingHistoryRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BookingAdapter(bookings)
        recyclerView.adapter = adapter

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("users").document(uid)
            .collection("bookings")
            .get()
            .addOnSuccessListener { result ->
                bookings.clear()
                bookings.addAll(result.map { it.toObject(Booking::class.java) })
                adapter.notifyDataSetChanged()
            }
    }
}
