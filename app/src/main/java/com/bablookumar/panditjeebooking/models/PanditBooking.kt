package com.bablookumar.panditjeebooking.models

data class PanditBooking(
    val id: String = "",
    val userName: String = "",   // who booked the puja
    val pujaName: String = "",
    val dateTime: String = "",   // "12 Aug 2025, 10:00 AM"
    val price: Double = 0.0,
    val status: String = "pending"  // pending / accepted / declined
)
