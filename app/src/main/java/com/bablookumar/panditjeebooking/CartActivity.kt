package com.bablookumar.panditjeebooking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.adapters.CartAdapter
import com.bablookumar.panditjeebooking.models.PujaService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var totalText: TextView
    private lateinit var payBtn: Button

    private lateinit var adapter: CartAdapter
    private val cartItems = mutableListOf<PujaService>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        totalText = findViewById(R.id.totalAmountText)
        payBtn = findViewById(R.id.payNowBtn)

        adapter = CartAdapter(cartItems) { item ->
            removeCartItem(item)
        }

        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartRecyclerView.adapter = adapter

        loadCartItems()

        payBtn.setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val totalAmount = cartItems.sumOf { it.price }
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("amount", totalAmount.toInt() * 100) // In paisa for Razorpay
            startActivity(intent)
        }
    }

    private fun loadCartItems() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("cart")
            .get()
            .addOnSuccessListener { result ->
                cartItems.clear()
                for (doc in result) {
                    val item = doc.toObject(PujaService::class.java)
                    cartItems.add(item)
                }
                adapter.notifyDataSetChanged()
                updateTotal()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load cart", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeCartItem(item: PujaService) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid)
            .collection("cart").document(item.id).delete()
            .addOnSuccessListener {
                cartItems.remove(item)
                adapter.notifyDataSetChanged()
                updateTotal()
                Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to remove item", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateTotal() {
        val total = cartItems.sumOf { it.price }
        totalText.text = "Total: ₹${total.toInt()}"
    }
}
