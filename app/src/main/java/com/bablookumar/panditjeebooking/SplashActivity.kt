package com.bablookumar.panditjeebooking

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = auth.currentUser

            if (currentUser == null) {
                // ❌ Not logged in → Go to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                // ✅ Logged in → Go to MainActivity
                startActivity(Intent(this, UserHomeActivity::class.java))
            }

            finish()
        }, 2000) // 2-second splash delay
    }
}
