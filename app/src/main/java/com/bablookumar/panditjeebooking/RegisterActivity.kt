package com.bablookumar.panditjeebooking

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // UI References
        val roleToggle = findViewById<RadioGroup>(R.id.roleToggle)
        val panditRadio = findViewById<RadioButton>(R.id.panditRadio)

        val nameEdit = findViewById<EditText>(R.id.nameEdit)
        val emailEdit = findViewById<EditText>(R.id.emailEdit)
        val phoneEdit = findViewById<EditText>(R.id.phoneEdit)
        val passwordEdit = findViewById<EditText>(R.id.passwordEdit)
        val confirmPasswordEdit = findViewById<EditText>(R.id.confirmPasswordEdit)

        val experienceEdit = findViewById<EditText>(R.id.experienceEdit)
        val cityEdit = findViewById<EditText>(R.id.cityEdit)
        val servicesEdit = findViewById<EditText>(R.id.servicesEdit)

        val togglePassBtn = findViewById<ImageView>(R.id.togglePassword)
        val toggleConfirmBtn = findViewById<ImageView>(R.id.toggleConfirmPassword)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Go to login page
        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Toggle password visibility
        togglePassBtn.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            passwordEdit.inputType = if (isPasswordVisible)
                InputType.TYPE_CLASS_TEXT
            else
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            passwordEdit.setSelection(passwordEdit.text.length)
        }

        toggleConfirmBtn.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            confirmPasswordEdit.inputType = if (isConfirmPasswordVisible)
                InputType.TYPE_CLASS_TEXT
            else
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            confirmPasswordEdit.setSelection(confirmPasswordEdit.text.length)
        }

        // Show extra fields if Pandit is selected
        roleToggle.setOnCheckedChangeListener { _, checkedId ->
            val isPandit = checkedId == R.id.panditRadio
            val visibility = if (isPandit) View.VISIBLE else View.GONE
            experienceEdit.visibility = visibility
            cityEdit.visibility = visibility
            servicesEdit.visibility = visibility
        }

        // Register Button
        registerBtn.setOnClickListener {
            val name = nameEdit.text.toString().trim()
            val email = emailEdit.text.toString().trim()
            val phone = phoneEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()
            val confirmPassword = confirmPasswordEdit.text.toString().trim()
            val role = if (panditRadio.isChecked) "pandit" else "user"

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()
            ) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            registerBtn.isEnabled = false

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = auth.currentUser?.uid ?: return@addOnSuccessListener

                    val userData = mutableMapOf<String, Any>(
                        "name" to name,
                        "email" to email,
                        "phone" to phone,
                        "role" to role
                    )

                    if (role == "pandit") {
                        userData["experience"] = experienceEdit.text.toString().trim()
                        userData["city"] = cityEdit.text.toString().trim()
                        userData["services"] = servicesEdit.text.toString().trim()
                    }

                    val collection = if (role == "pandit") "pandits" else "users"
                    db.collection(collection).document(uid).set(userData)
                        .addOnSuccessListener {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()

                            // ✅ Redirect to login
                            auth.signOut()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            progressBar.visibility = View.GONE
                            registerBtn.isEnabled = true
                            Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    progressBar.visibility = View.GONE
                    registerBtn.isEnabled = true
                    Toast.makeText(this, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
