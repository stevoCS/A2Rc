package com.example.rentacar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Rental record data model
 * @param carId Car ID
 * @param days Rental days
 * @param totalCost Total cost
 */
@Parcelize
data class RentalRecord(
    val carId: String,
    val days: Int,
    val totalCost: Int
) : Parcelable

