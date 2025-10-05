package com.example.rentacar.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.rentacar.model.Car
import com.example.rentacar.model.RentalRecord
import com.example.rentacar.ui.detail.DetailActivity

/**
 * Custom ActivityResultContract for handling data transfer between Activities
 */

/**
 * Rent car result contract
 */
class RentCarContract : ActivityResultContract<Car, RentalRecord?>() {
    
    override fun createIntent(context: Context, input: Car): Intent {
        return Intent(context, DetailActivity::class.java).apply {
            putExtra("car", input)
        }
    }
    
    override fun parseResult(resultCode: Int, intent: Intent?): RentalRecord? {
        return if (resultCode == Activity.RESULT_OK) {
            intent?.getParcelableExtra("rental_record")
        } else {
            null
        }
    }
}

