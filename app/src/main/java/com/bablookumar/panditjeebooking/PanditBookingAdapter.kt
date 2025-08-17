package com.bablookumar.panditjeebooking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.R
import com.bablookumar.panditjeebooking.models.PanditBooking

class PanditBookingAdapter(
    private val bookingList: List<PanditBooking>,
    private val onAction: (PanditBooking, String) -> Unit // accept/decline
) : RecyclerView.Adapter<PanditBookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPujaName: TextView = itemView.findViewById(R.id.tvPujaName)
        val tvPujaDate: TextView = itemView.findViewById(R.id.tvPujaDate)
        val tvPujaPrice: TextView = itemView.findViewById(R.id.tvPujaPrice)
        val btnAccept: Button = itemView.findViewById(R.id.btnAccept)
        val btnDecline: Button = itemView.findViewById(R.id.btnDecline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pandit_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookingList[position]

        holder.tvPujaName.text = booking.pujaName
        holder.tvPujaDate.text = booking.dateTime
        holder.tvPujaPrice.text = "₹${booking.price}"

        holder.btnAccept.setOnClickListener { onAction(booking, "accepted") }
        holder.btnDecline.setOnClickListener { onAction(booking, "declined") }
    }

    override fun getItemCount(): Int = bookingList.size
}
