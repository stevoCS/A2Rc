package com.example.rentacar.ui.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.rentacar.R
import com.example.rentacar.databinding.ActivityDetailBinding
import com.example.rentacar.model.Car
import com.example.rentacar.model.RentalRecord
import com.example.rentacar.util.formatCredits
import com.example.rentacar.util.showSnackbar

/**
 * Detail Activity, displays car details and rental functionality
 */
class DetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private var car: Car? = null
    private var currentBalance: Int = 500
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupBackPressedHandler()
        loadCarData()
        setupClickListeners()
        setupObservers()
    }

    private fun setupBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(RESULT_CANCELED)
                finish()
            }
        })
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    private fun loadCarData() {
        car = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("car", Car::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("car")
        }
        currentBalance = intent.getIntExtra("balance", 500)
        
        if (car == null) {
            finish()
            return
        }
        
        viewModel.setCarAndBalance(car!!, currentBalance)
        displayCar(car!!)
    }

    private fun displayCar(car: Car) {
        binding.carImage.setImageResource(car.imageRes)
        binding.carName.text = car.getDisplayName()
        binding.carModelYear.text = car.getYearText()
        binding.carKm.text = car.getKmText()
        binding.carDailyCost.text = car.getDailyCostText()
        binding.carRating.rating = car.rating
        binding.carRatingText.text = car.getRatingText()

        // Set slider initial value
        binding.daysSlider.value = 1f
        updateCostDisplay()
    }
    
    private fun setupClickListeners() {
        // Rating slider
        binding.carRating.setOnRatingBarChangeListener { _, _, _ ->
            // Rating is read-only, no modification allowed
        }

        // Days slider
        binding.daysSlider.addOnChangeListener { _, value, _ ->
            val days = value.toInt()
            viewModel.updateDays(days)
        }

        // Cancel button
        binding.cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        // Save button
        binding.saveButton.setOnClickListener {
            saveRental()
        }
    }

    private fun setupObservers() {
        // Observe selected days
        viewModel.selectedDays.observe(this, Observer { days ->
            binding.selectedDaysText.text = "Selected Days: $days"
        })

        // Observe total cost
        viewModel.totalCost.observe(this, Observer { total ->
            binding.totalCostText.text = "Total Cost: ${total.formatCredits()}"
        })

        // Observe is valid
        viewModel.isValid.observe(this, Observer { isValid ->
            binding.saveButton.isEnabled = isValid
            if (isValid) {
                binding.totalCostText.setTextColor(getColor(R.color.primary_blue))
            } else {
                binding.totalCostText.setTextColor(getColor(R.color.error_red))
            }
        })

        // Observe error message
        viewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                binding.root.showSnackbar(it)
                viewModel.clearErrorMessage()
            }
        })
    }
    
    private fun updateCostDisplay() {
        val car = this.car ?: return
        val days = viewModel.selectedDays.value ?: 1
        val totalCost = car.dailyCost * days
        
        binding.selectedDaysText.text = "Selected Days: $days"
        binding.totalCostText.text = "Total Cost: ${totalCost.formatCredits()}"
    }
    
    private fun saveRental() {
        val rentalRecord = viewModel.createRentalRecord()

        if (rentalRecord != null) {
            // Return result
            val resultIntent = Intent().apply {
                putExtra("rental_record", rentalRecord)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        } else {
            binding.root.showSnackbar("Cannot save rental. Please check the requirements.")
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                setResult(RESULT_CANCELED)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

