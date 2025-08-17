package com.bablookumar.panditjeebooking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.R
import com.bablookumar.panditjeebooking.models.PujaService
import com.squareup.picasso.Picasso

class CartAdapter(
    private val items: List<PujaService>,
    private val onRemoveClick: (PujaService) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.cartItemTitle)
        val priceText: TextView = view.findViewById(R.id.cartItemPrice)
        val imageView: ImageView = view.findViewById(R.id.cartItemImage)
        val removeBtn: Button = view.findViewById(R.id.removeFromCartBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.titleText.text = item.title
        holder.priceText.text = "₹${item.price.toInt()}"
        Picasso.get().load(item.imageUrl).into(holder.imageView)

        holder.removeBtn.setOnClickListener {
            onRemoveClick(item)
        }
    }
}
