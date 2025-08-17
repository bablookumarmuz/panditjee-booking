package com.bablookumar.panditjeebooking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OrderConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        val goHomeBtn = findViewById<Button>(R.id.goHomeBtn)
        goHomeBtn.setOnClickListener {
            startActivity(Intent(this, UserHomeActivity::class.java))
            finish()
        }
    }
}
