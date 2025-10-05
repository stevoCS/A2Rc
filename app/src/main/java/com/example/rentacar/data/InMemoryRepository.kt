package com.example.rentacar.data

import com.example.rentacar.R
import com.example.rentacar.model.Car
import com.example.rentacar.model.RentalRecord

/**
 * In-memory data repository, manages car data and rental records (Singleton pattern)
 */
class InMemoryRepository private constructor() {

    companion object {
        @Volatile
        private var instance: InMemoryRepository? = null

        fun getInstance(): InMemoryRepository {
            return instance ?: synchronized(this) {
                instance ?: InMemoryRepository().also { instance = it }
            }
        }
    }

    // Base car data (immutable)
    private val carsBase = listOf(
        Car("1", "BMW", "i7", 2024, 4.9f, 5000, 150, R.drawable.bmw_i7),
        Car("2", "BMW", "X1", 2023, 4.6f, 12000, 95, R.drawable.bmw_x1),
        Car("3", "Mercedes-Benz", "S 500", 2024, 4.8f, 8000, 140, R.drawable.mercedes_benz_s_500),
        Car("4", "Porsche", "911", 2023, 5.0f, 6000, 180, R.drawable.porsche_911),
        Car("5", "Tesla", "Model 3", 2025, 4.7f, 10000, 100, R.drawable.tesla_model_3),
        Car("6", "Tesla", "Model Y", 2025, 4.7f, 9000, 110, R.drawable.tesla_model_y)
    )

    // Available cars list (mutable)
    val availableCars = carsBase.toMutableList()

    // Rented cars records
    val rentedCars = mutableListOf<RentalRecord>()

    // Favorite car ID set
    val favorites = mutableSetOf<String>()

    /**
     * Get car information by ID
     */
    fun getCarById(carId: String): Car? {
        return carsBase.find { it.id == carId }
    }

    /**
     * Get current car by index
     */
    fun getCurrentCar(index: Int): Car? {
        return if (index in availableCars.indices) availableCars[index] else null
    }

    /**
     * Rent a car
     * @param carId Car ID
     * @param days Rental days
     * @return Rental record, or null if failed
     */
    fun rent(carId: String, days: Int): RentalRecord? {
        val car = getCarById(carId) ?: return null

        // Check if car is still available for rent
        if (!availableCars.any { it.id == carId }) {
            return null
        }

        val totalCost = car.dailyCost * days
        val rentalRecord = RentalRecord(carId, days, totalCost)

        // Remove from available list
        availableCars.removeAll { it.id == carId }

        // Add to rented records
        rentedCars.add(rentalRecord)

        return rentalRecord
    }

    /**
     * Cancel rental (re-add car to available list)
     */
    fun cancelRent(carId: String): Boolean {
        val car = getCarById(carId) ?: return false

        // Remove from rented records
        val removed = rentedCars.removeAll { it.carId == carId }

        // Re-add to available list (if not already in list)
        if (removed && !availableCars.any { it.id == carId }) {
            availableCars.add(car)
        }

        return removed
    }

    /**
     * Toggle favorite status
     */
    fun toggleFavorite(carId: String): Boolean {
        return if (favorites.contains(carId)) {
            favorites.remove(carId)
            false
        } else {
            favorites.add(carId)
            true
        }
    }

    /**
     * Check if car is favorited
     */
    fun isFavorite(carId: String): Boolean {
        return favorites.contains(carId)
    }

    /**
     * Get list of favorite cars
     */
    fun getFavoriteCars(): List<Car> {
        return carsBase.filter { favorites.contains(it.id) }
    }

    /**
     * Reset data (for testing)
     */
    fun resetData() {
        availableCars.clear()
        availableCars.addAll(carsBase)
        rentedCars.clear()
        favorites.clear()
    }
}

