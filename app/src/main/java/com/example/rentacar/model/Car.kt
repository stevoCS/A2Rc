package com.example.rentacar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Car data model, uses @Parcelize annotation to simplify Parcelable implementation
 * @param id Car unique identifier
 * @param name Car name
 * @param model Car model
 * @param year Year
 * @param rating Rating (1.0f - 5.0f)
 * @param km Mileage
 * @param dailyCost Daily rental cost (credits)
 * @param imageRes Image resource ID
 */
@Parcelize
data class Car(
    val id: String,
    val name: String,
    val model: String,
    val year: Int,
    val rating: Float,
    val km: Int,
    val dailyCost: Int,
    val imageRes: Int
) : Parcelable {

    /**
     * Get car display name
     */
    fun getDisplayName(): String = "$name $model"

    /**
     * Get year display text
     */
    fun getYearText(): String = "Year: $year"

    /**
     * Get mileage display text
     */
    fun getKmText(): String = "Mileage: $km km"

    /**
     * Get daily cost display text
     */
    fun getDailyCostText(): String = "Daily Cost: $dailyCost credits"

    /**
     * Get rating display text
     */
    fun getRatingText(): String = "Rating: ${String.format("%.1f", rating)}/5.0"
}

