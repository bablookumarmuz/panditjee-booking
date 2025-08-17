package com.bablookumar.panditjeebooking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.R
import com.bablookumar.panditjeebooking.models.PujaService
import com.bumptech.glide.Glide

class PujaServiceAdapter(
    private val serviceList: List<PujaService>,
    private val onItemClick: (PujaService) -> Unit
) : RecyclerView.Adapter<PujaServiceAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val serviceImage: ImageView = view.findViewById(R.id.serviceImage)
        val serviceTitle: TextView = view.findViewById(R.id.serviceTitle)
        val servicePrice: TextView = view.findViewById(R.id.servicePrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_puja_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = serviceList[position]
        holder.serviceTitle.text = service.title
        holder.servicePrice.text = "₹${service.price.toInt()}"

        Glide.with(holder.itemView.context)
            .load(service.imageUrl)
            .placeholder(R.drawable.sample_service)
            .into(holder.serviceImage)

        holder.itemView.setOnClickListener {
            onItemClick(service)
        }
    }

    override fun getItemCount(): Int = serviceList.size
}
