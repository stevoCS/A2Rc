package com.example.rentacar.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Extension functions utility class
 */

/**
 * Format credits display
 */
fun Int.formatCredits(): String = "$this credits"

/**
 * Format days display
 */
fun Int.formatDays(): String = if (this == 1) "1 day" else "$this days"

/**
 * Format rating display
 */
fun Float.formatRating(): String = "%.1f/5.0".format(this)

/**
 * Extension function to show Snackbar
 */
fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

/**
 * Show Snackbar with Action
 */
fun View.showSnackbarWithAction(
    message: String,
    actionText: String,
    action: () -> Unit,
    duration: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, message, duration)
        .setAction(actionText) { action() }
        .show()
}

