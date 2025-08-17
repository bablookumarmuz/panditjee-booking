package com.bablookumar.panditjeebooking

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private var isPasswordVisible = false

    // ✅ Modern Activity Result API
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        checkUserRole(auth.currentUser!!.uid)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Sign-In Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(this, "Google Sign-In error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // If already logged in → check role
        if (auth.currentUser != null) {
            checkUserRole(auth.currentUser!!.uid)
            return
        }

        // Views
        val emailEdit = findViewById<EditText>(R.id.emailEdit)
        val passwordEdit = findViewById<EditText>(R.id.passwordEdit)
        val togglePassBtn = findViewById<ImageView>(R.id.togglePassword)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val forgotPass = findViewById<TextView>(R.id.forgotPassword)
        val googleLogin = findViewById<ImageView>(R.id.googleLogin)
        val createAccount = findViewById<TextView>(R.id.createAccount)

        // Toggle password visibility
        togglePassBtn.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            passwordEdit.inputType = if (isPasswordVisible) {
                InputType.TYPE_CLASS_TEXT
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            passwordEdit.setSelection(passwordEdit.text.length)
        }

        // Login button
        loginBtn.setOnClickListener {
            val email = emailEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    checkUserRole(auth.currentUser!!.uid)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // Forgot password
        forgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        // Register
        createAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Google Sign-In setup
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleLogin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    // ✅ Role-based redirect
    private fun checkUserRole(uid: String) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    startActivity(Intent(this, UserHomeActivity::class.java))
                    finish()
                } else {
                    db.collection("pandits").document(uid).get()
                        .addOnSuccessListener { panditSnap ->
                            if (panditSnap.exists()) {
                                startActivity(Intent(this, PanditHomeActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Role not found. Please register again.", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                            }
                        }
                }
            }
    }
}
