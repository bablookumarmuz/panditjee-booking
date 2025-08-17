package com.bablookumar.panditjeebooking

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.adapters.SamagriCartAdapter
import com.bablookumar.panditjeebooking.models.SamagriItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SamagriCartActivity : AppCompatActivity() {

    private lateinit var cartRecycler: RecyclerView
    private lateinit var totalText: TextView
    private lateinit var checkoutBtn: Button
    private lateinit var adapter: SamagriCartAdapter
    private val cartItems = mutableListOf<SamagriItem>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_samagri_cart)

        cartRecycler = findViewById(R.id.samagriCartRecycler)
        totalText = findViewById(R.id.samagriCartTotal)
        checkoutBtn = findViewById(R.id.samagriCheckoutBtn)

        adapter = SamagriCartAdapter(cartItems) { item, newQty ->
            updateQuantity(item, newQty)
        }
        cartRecycler.layoutManager = LinearLayoutManager(this)
        cartRecycler.adapter = adapter

        checkoutBtn.setOnClickListener {
            Toast.makeText(this, "Checkout flow to be implemented", Toast.LENGTH_SHORT).show()
        }

        loadCart()
    }

    private fun loadCart() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("samagri_cart")
            .get()
            .addOnSuccessListener { result ->
                cartItems.clear()
                for (doc in result) {
                    val item = doc.toObject(SamagriItem::class.java)
                    cartItems.add(item)
                }
                adapter.notifyDataSetChanged()
                calculateTotal()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load cart", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateQuantity(item: SamagriItem, newQty: Int) {
        val uid = auth.currentUser?.uid ?: return
        val docRef = db.collection("users").document(uid)
            .collection("samagri_cart").document(item.id)

        if (newQty <= 0) {
            docRef.delete()
                .addOnSuccessListener {
                    cartItems.removeIf { it.id == item.id }
                    adapter.notifyDataSetChanged()
                    calculateTotal()
                }
        } else {
            val updatedItem = item.copy(quantity = newQty.toString())
            docRef.set(updatedItem)
                .addOnSuccessListener {
                    val index = cartItems.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        cartItems[index] = updatedItem
                        adapter.notifyItemChanged(index)
                        calculateTotal()
                    }
                }
        }
    }

    private fun calculateTotal() {
        var total = 0.0
        for (item in cartItems) {
            val qty = item.quantity.toIntOrNull() ?: 1
            total += item.price * qty
        }
        totalText.text = "Total: ₹${"%.2f".format(total)}"
    }
}
