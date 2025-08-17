package com.bablookumar.panditjeebooking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bablookumar.panditjeebooking.models.PujaService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity(), PaymentResultListener {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var amount = 0
    private var paymentType = "puja" // "puja" or "samagri"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        amount = intent.getIntExtra("amount", 0)
        paymentType = intent.getStringExtra("type") ?: "puja"

        if (amount <= 0) {
            Toast.makeText(this, "Invalid payment amount", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        startPayment()
    }

    private fun startPayment() {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_YourTestKeyHere") // 🔑 Replace with Razorpay Test Key
        checkout.setImage(R.drawable.logo) // ✅ Your app logo in payment screen

        val userEmail = auth.currentUser?.email ?: "test@example.com"

        try {
            val options = JSONObject()
            options.put("name", "PanditJee Booking")
            options.put("description", if (paymentType == "puja") "Puja Booking Payment" else "Samagri Order Payment")
            options.put("currency", "INR")
            options.put("amount", amount) // amount in paise
            options.put("prefill.email", userEmail)

            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()
        if (paymentType == "puja") {
            savePujaBooking()
        } else {
            saveSamagriOrder()
        }
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_LONG).show()
        finish()
    }

    // ------------------------ SAVE PUJA BOOKINGS ------------------------
    private fun savePujaBooking() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users").document(uid).collection("cart").get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show()
                    finish()
                    return@addOnSuccessListener
                }

                val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                val dateTime = formatter.format(Date())
                val bookingsRef = db.collection("bookings")

                val batch = db.batch()
                result.documents.forEach { doc ->
                    val item = doc.toObject(PujaService::class.java) ?: return@forEach
                    val booking = mapOf(
                        "uid" to uid,
                        "pujaType" to item.title,
                        "panditName" to "To be assigned",
                        "dateTime" to dateTime,
                        "price" to item.price,
                        "rating" to null
                    )
                    val newDoc = bookingsRef.document()
                    batch.set(newDoc, booking)
                    batch.delete(doc.reference) // clear cart
                }

                batch.commit().addOnSuccessListener {
                    Toast.makeText(this, "Booking confirmed!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, OrderConfirmationActivity::class.java))
                    finish()
                }
            }
    }

    // ------------------------ SAVE SAMAGRI ORDERS ------------------------
    private fun saveSamagriOrder() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users").document(uid).collection("samagri_cart").get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(this, "Samagri cart is empty!", Toast.LENGTH_SHORT).show()
                    finish()
                    return@addOnSuccessListener
                }

                val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                val dateTime = formatter.format(Date())
                val ordersRef = db.collection("samagri_orders")

                val batch = db.batch()
                result.documents.forEach { doc ->
                    val itemData = doc.data ?: return@forEach
                    val order = mapOf(
                        "uid" to uid,
                        "productName" to itemData["name"],
                        "quantity" to itemData["quantity"],
                        "price" to itemData["price"],
                        "orderDate" to dateTime
                    )
                    val newDoc = ordersRef.document()
                    batch.set(newDoc, order)
                    batch.delete(doc.reference) // clear cart
                }

                batch.commit().addOnSuccessListener {
                    Toast.makeText(this, "Samagri order placed!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, OrderConfirmationActivity::class.java))
                    finish()
                }
            }
    }
}
