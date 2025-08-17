package com.bablookumar.panditjeebooking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.R
import com.bablookumar.panditjeebooking.models.Booking

class BookingAdapter(private val bookings: List<Booking>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val panditName: TextView = view.findViewById(R.id.panditName)
        val pujaType: TextView = view.findViewById(R.id.pujaType)
        val dateTime: TextView = view.findViewById(R.id.dateTime)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.panditName.text = booking.panditName
        holder.pujaType.text = booking.pujaType
        holder.dateTime.text = booking.dateTime
        holder.ratingBar.rating = booking.rating ?: 0f
    }

    override fun getItemCount(): Int = bookings.size
}
