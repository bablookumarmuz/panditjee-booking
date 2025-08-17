package com.bablookumar.panditjeebooking

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bablookumar.panditjeebooking.adapters.PujaServiceAdapter
import com.bablookumar.panditjeebooking.models.PujaService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserHomeActivity : AppCompatActivity() {

    private lateinit var greetingText: TextView
    private lateinit var featuredRecyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var filterIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        // Initialize views
        greetingText = findViewById(R.id.greetingText)
        featuredRecyclerView = findViewById(R.id.featuredRecyclerView)
        filterIcon = findViewById(R.id.filterBtn)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // UI
        loadUserInfo()
        setupRecyclerViews()
        loadPujaServices()

        // Filter icon click
        filterIcon.setOnClickListener {
            Toast.makeText(this, "Filter clicked", Toast.LENGTH_SHORT).show()
        }

        // Bottom Nav
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> true // Already here
                R.id.menu_samagri -> {
                    startActivity(Intent(this, SamagriActivity::class.java))
                    finish()
                    true
                }
                R.id.menu_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

    }

    private fun loadUserInfo() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val name = doc.getString("name") ?: "User"
                val city = doc.getString("city") ?: ""
                greetingText.text = "Welcome, $name\n📍 $city"
            }
            .addOnFailureListener {
                greetingText.text = "Welcome!"
            }
    }

    private fun setupRecyclerViews() {
        featuredRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadPujaServices() {
        db.collection("puja_services").get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { it.toObject(PujaService::class.java) }
                val adapter = PujaServiceAdapter(list) { service ->
                    val intent = Intent(this, PujaDetailActivity::class.java)
                    intent.putExtra("puja_service", service)
                    startActivity(intent)
                }
                featuredRecyclerView.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load services", Toast.LENGTH_SHORT).show()
            }
    }
}