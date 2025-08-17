package com.bablookumar.panditjeebooking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.R
import com.bablookumar.panditjeebooking.models.SamagriOrder
import com.squareup.picasso.Picasso

class SamagriOrderAdapter(
    private val items: List<SamagriOrder>
) : RecyclerView.Adapter<SamagriOrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.orderItemName)
        val quantityText: TextView = view.findViewById(R.id.orderItemQuantity)
        val priceText: TextView = view.findViewById(R.id.orderItemPrice)
        val imageView: ImageView = view.findViewById(R.id.orderItemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_samagri_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = items[position]
        holder.nameText.text = item.name
        holder.quantityText.text = "Qty: ${item.quantity}"
        holder.priceText.text = "₹${item.price.toInt()}"
        Picasso.get().load(item.imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int = items.size
}
