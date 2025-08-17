package com.bablookumar.panditjeebooking

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.adapters.SamagriAdapter
import com.bablookumar.panditjeebooking.models.SamagriItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SamagriActivity : AppCompatActivity() {

    private lateinit var searchBar: EditText
    private lateinit var samagriRecycler: RecyclerView
    private lateinit var adapter: SamagriAdapter
    private val itemList = mutableListOf<SamagriItem>()
    private val filteredList = mutableListOf<SamagriItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_samagri)

        searchBar = findViewById(R.id.searchProducts)
        samagriRecycler = findViewById(R.id.samagriRecycler)

        adapter = SamagriAdapter(filteredList) { item ->
            // Add to cart logic
            addToCart(item)
        }

        samagriRecycler.layoutManager = GridLayoutManager(this, 2)
        samagriRecycler.adapter = adapter

        loadSamagri()

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        findViewById<BottomNavigationView>(R.id.bottomNav).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, UserHomeActivity::class.java))
                    true
                }
                R.id.menu_samagri -> true
                R.id.menu_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun filterList(query: String) {
        val lowerQuery = query.lowercase()
        filteredList.clear()
        filteredList.addAll(itemList.filter {
            it.name.lowercase().contains(lowerQuery)
        })
        adapter.notifyDataSetChanged()
    }

    private fun loadSamagri() {
        FirebaseFirestore.getInstance().collection("samagri_items").get()
            .addOnSuccessListener { result ->
                itemList.clear()
                filteredList.clear()
                for (doc in result) {
                    val item = doc.toObject(SamagriItem::class.java)
                    itemList.add(item)
                    filteredList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load items", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addToCart(item: SamagriItem) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("samagri_cart")
            .document(item.id)
            .set(item)
            .addOnSuccessListener {
                Toast.makeText(this, "${item.name} added to cart", Toast.LENGTH_SHORT).show()
            }
    }
}
