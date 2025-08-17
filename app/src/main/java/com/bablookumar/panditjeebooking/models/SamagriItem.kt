package com.bablookumar.panditjeebooking.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SamagriItem(
    val id: String = "",
    val name: String = "",
    val quantity: String = "",
    val price: Double = 0.0,
    val imageUrl: String = ""
) : Parcelable
