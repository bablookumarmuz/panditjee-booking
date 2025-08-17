package com.bablookumar.panditjeebooking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bablookumar.panditjeebooking.models.PujaService
import com.bumptech.glide.Glide

class PujaDetailActivity : AppCompatActivity() {

    private lateinit var service: PujaService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puja_detail)

        // ✅ Updated to use type-safe API to avoid deprecation warning
        service = intent.getParcelableExtra("puja_service", PujaService::class.java) ?: return

        val image = findViewById<ImageView>(R.id.detailImage)
        val title = findViewById<TextView>(R.id.detailTitle)
        val description = findViewById<TextView>(R.id.detailDescription)
        val price = findViewById<TextView>(R.id.detailPrice)
        val bookBtn = findViewById<Button>(R.id.btnBookNow)
        val cartBtn = findViewById<Button>(R.id.btnAddToCart)

        Glide.with(this)
            .load(service.imageUrl)
            .placeholder(R.drawable.sample_service)
            .into(image)

        title.text = service.title
        description.text = service.description
        price.text = "Price: ₹${service.price}"

        // Book Now → PaymentActivity
        bookBtn.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("puja_service", service)
            startActivity(intent)
        }

        // Add to Cart → CartActivity
        cartBtn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("puja_service", service)
            startActivity(intent)
        }
    }
}
