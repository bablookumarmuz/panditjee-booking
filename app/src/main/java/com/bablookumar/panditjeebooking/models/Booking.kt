package com.bablookumar.panditjeebooking.models

data class Booking(
    val id: String = "",
    val panditName: String = "",
    val pujaType: String = "",
    val dateTime: String = "",  // Format: "12 Aug 2025, 10:00 AM"
    val rating: Float? = null   // Optional
)
