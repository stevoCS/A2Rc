package com.example.rentacar.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rentacar.model.Car
import com.example.rentacar.model.RentalRecord

/**
 * Detail view model, manages rental details and validation logic
 */
class DetailViewModel : ViewModel() {

    private var car: Car? = null
    private var currentBalance: Int = 500

    // Selected rental days
    private val _selectedDays = MutableLiveData(1)
    val selectedDays: LiveData<Int> = _selectedDays

    // Total cost
    private val _totalCost = MutableLiveData(0)
    val totalCost: LiveData<Int> = _totalCost

    // Is valid (can be saved)
    private val _isValid = MutableLiveData(false)
    val isValid: LiveData<Boolean> = _isValid

    // Error message
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    /**
     * Set car and balance
     */
    fun setCarAndBalance(car: Car, balance: Int) {
        this.car = car
        this.currentBalance = balance
        updateTotalCost()
    }

    /**
     * Update rental days
     */
    fun updateDays(days: Int) {
        _selectedDays.value = days
        updateTotalCost()
    }

    /**
     * Update total cost
     */
    private fun updateTotalCost() {
        val car = this.car ?: return
        val days = _selectedDays.value ?: 1
        val total = car.dailyCost * days

        _totalCost.value = total

        // Validation
        val isValid = total <= 400 && total <= currentBalance
        _isValid.value = isValid

        if (!isValid) {
            when {
                total > 400 -> _errorMessage.value = "Single rental limit: Cannot exceed 400 credits"
                total > currentBalance -> _errorMessage.value = "Insufficient credits"
            }
        } else {
            _errorMessage.value = null
        }
    }

    /**
     * Create rental record
     */
    fun createRentalRecord(): RentalRecord? {
        val car = this.car ?: return null
        val days = _selectedDays.value ?: 1
        val total = _totalCost.value ?: 0

        return if (_isValid.value == true) {
            RentalRecord(car.id, days, total)
        } else {
            null
        }
    }

    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}

