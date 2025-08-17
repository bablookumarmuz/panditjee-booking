package com.bablookumar.panditjeebooking

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var nameField: EditText
    private lateinit var cityField: EditText
    private lateinit var saveBtn: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        nameField = findViewById(R.id.editName)
        cityField = findViewById(R.id.editCity)
        saveBtn = findViewById(R.id.saveBtn)

        loadUserData()

        saveBtn.setOnClickListener {
            val name = nameField.text.toString().trim()
            val city = cityField.text.toString().trim()
            val uid = auth.currentUser?.uid ?: return@setOnClickListener

            val updates = mapOf("name" to name, "city" to city)
            db.collection("users").document(uid).update(updates)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }

    private fun loadUserData() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener {
                nameField.setText(it.getString("name") ?: "")
                cityField.setText(it.getString("city") ?: "")
            }
    }
}
