package com.bablookumar.panditjeebooking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.R
import com.bablookumar.panditjeebooking.models.SamagriItem
import com.squareup.picasso.Picasso

class SamagriCartAdapter(
    private val items: MutableList<SamagriItem>,
    private val onQuantityChanged: (SamagriItem, Int) -> Unit
) : RecyclerView.Adapter<SamagriCartAdapter.SamagriCartViewHolder>() {

    inner class SamagriCartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.samagriItemImage)
        val name: TextView = view.findViewById(R.id.samagriItemName)
        val price: TextView = view.findViewById(R.id.samagriItemPrice)
        val quantityText: TextView = view.findViewById(R.id.quantityText)
        val incrementBtn: ImageButton = view.findViewById(R.id.incrementBtn)
        val decrementBtn: ImageButton = view.findViewById(R.id.decrementBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SamagriCartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_samagri_cart, parent, false)
        return SamagriCartViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SamagriCartViewHolder, position: Int) {
        val item = items[position]

        holder.name.text = item.name
        val qty = item.quantity.toIntOrNull() ?: 1
        holder.price.text = "₹${item.price * qty}"
        holder.quantityText.text = qty.toString()
        Picasso.get().load(item.imageUrl).into(holder.image)

        holder.incrementBtn.setOnClickListener {
            onQuantityChanged(item, qty + 1)
        }

        holder.decrementBtn.setOnClickListener {
            onQuantityChanged(item, qty - 1)
        }
    }
}
