package com.bablookumar.panditjeebooking.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PujaService(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = ""
) : Parcelable
