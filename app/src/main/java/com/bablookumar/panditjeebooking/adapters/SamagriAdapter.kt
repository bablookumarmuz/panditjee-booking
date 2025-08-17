package com.bablookumar.panditjeebooking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.R
import com.bablookumar.panditjeebooking.models.SamagriItem
import com.squareup.picasso.Picasso

class SamagriAdapter(
    private val list: List<SamagriItem>,
    private val onAddClick: (SamagriItem) -> Unit
) : RecyclerView.Adapter<SamagriAdapter.SamagriViewHolder>() {

    inner class SamagriViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.samImage)
        val name: TextView = view.findViewById(R.id.samName)
        val quantity: TextView = view.findViewById(R.id.samQuantity)
        val price: TextView = view.findViewById(R.id.samPrice)
        val addBtn: Button = view.findViewById(R.id.samAddBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SamagriViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_samagri, parent, false)
        return SamagriViewHolder(view)
    }

    override fun onBindViewHolder(holder: SamagriViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
        holder.quantity.text = "Quantity ${item.quantity}"
        holder.price.text = "₹${item.price}"
        Picasso.get().load(item.imageUrl).into(holder.image)
        holder.addBtn.setOnClickListener { onAddClick(item) }
    }

    override fun getItemCount() = list.size
}
