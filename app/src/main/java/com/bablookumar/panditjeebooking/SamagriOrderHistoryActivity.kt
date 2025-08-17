package com.bablookumar.panditjeebooking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.adapters.SamagriOrderAdapter
import com.bablookumar.panditjeebooking.models.SamagriOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SamagriOrderHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SamagriOrderAdapter
    private val orders = mutableListOf<SamagriOrder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_samagri_order_history)

        recyclerView = findViewById(R.id.orderHistoryRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SamagriOrderAdapter(orders)
        recyclerView.adapter = adapter

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("users").document(uid)
            .collection("samagri_orders")
            .get()
            .addOnSuccessListener { result ->
                orders.clear()
                orders.addAll(result.map { it.toObject(SamagriOrder::class.java) })
                adapter.notifyDataSetChanged()
            }
    }
}
