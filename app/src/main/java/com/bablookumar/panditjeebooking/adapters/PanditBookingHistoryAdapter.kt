package com.bablookumar.panditjeebooking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.R
import com.bablookumar.panditjeebooking.models.PanditBooking

class PanditBookingHistoryAdapter(
    private val bookingList: List<PanditBooking>
) : RecyclerView.Adapter<PanditBookingHistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPujaName: TextView = itemView.findViewById(R.id.tvPujaName)
        val tvPujaDate: TextView = itemView.findViewById(R.id.tvPujaDate)
        val tvPujaPrice: TextView = itemView.findViewById(R.id.tvPujaPrice)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pandit_booking_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val booking = bookingList[position]
        holder.tvPujaName.text = booking.pujaName
        holder.tvPujaDate.text = booking.dateTime
        holder.tvPujaPrice.text = "₹${booking.price}"
        holder.tvStatus.text = booking.status
    }

    override fun getItemCount(): Int = bookingList.size
}
