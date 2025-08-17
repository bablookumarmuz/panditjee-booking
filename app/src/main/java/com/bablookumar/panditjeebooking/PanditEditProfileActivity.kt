package com.bablookumar.panditjeebooking

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PanditEditProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etCity: EditText
    private lateinit var etExperience: EditText
    private lateinit var btnSave: Button

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pandit_edit_profile)

        etName = findViewById(R.id.etName)
        etCity = findViewById(R.id.etCity)
        etExperience = findViewById(R.id.etExperience)
        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener { saveProfile() }
    }

    private fun saveProfile() {
        val name = etName.text.toString().trim()
        val city = etCity.text.toString().trim()
        val experience = etExperience.text.toString().trim()

        val uid = auth.currentUser?.uid ?: return
        val panditRef = database.child("pandits").child(uid)

        val updates = mapOf(
            "name" to name,
            "city" to city,
            "experience" to experience
        )

        panditRef.updateChildren(updates).addOnSuccessListener {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
