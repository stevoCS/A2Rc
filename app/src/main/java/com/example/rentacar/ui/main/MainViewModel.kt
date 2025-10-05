package com.example.rentacar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rentacar.data.InMemoryRepository
import com.example.rentacar.model.Car
import com.example.rentacar.model.RentalRecord

/**
 * Main view model, manages car data and user state
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = InMemoryRepository.getInstance()

    // Current balance
    private val _balance = MutableLiveData(500)
    val balance: LiveData<Int> = _balance

    // Current displayed car index
    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    // Current displayed car
    private val _currentCar = MutableLiveData<Car?>()
    val currentCar: LiveData<Car?> = _currentCar

    // Filtered and sorted car list
    private val _filteredSortedList = MutableLiveData<List<Car>>()
    val filteredSortedList: LiveData<List<Car>> = _filteredSortedList

    // Favorite cars list
    private val _favoriteCars = MutableLiveData<List<Car>>()
    val favoriteCars: LiveData<List<Car>> = _favoriteCars

    // Search query
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    // Sort type
    private val _sortType = MutableLiveData(SortType.RATING)
    val sortType: LiveData<SortType> = _sortType

    // Theme mode
    private val _isDarkTheme = MutableLiveData<Boolean>()
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    // Message notification
    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    init {
        // Load theme settings from SharedPreferences
        val sharedPrefs = application.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
        _isDarkTheme.value = sharedPrefs.getBoolean("is_dark_theme", false)

        loadCars()
        updateCurrentCar()
        updateFavoriteCars()
    }

    /**
     * Load car data
     */
    private fun loadCars() {
        _filteredSortedList.value = repository.availableCars
    }

    /**
     * Update current displayed car
     */
    private fun updateCurrentCar() {
        val cars = _filteredSortedList.value ?: emptyList()
        val index = _currentIndex.value ?: 0

        if (cars.isNotEmpty()) {
            val actualIndex = index % cars.size
            _currentIndex.value = actualIndex
            _currentCar.value = cars[actualIndex]
        } else {
            _currentCar.value = null
        }
    }

    /**
     * Update favorite cars list
     */
    private fun updateFavoriteCars() {
        _favoriteCars.value = repository.getFavoriteCars()
    }

    /**
     * Search cars
     */
    fun search(query: String) {
        _searchQuery.value = query
        val cars = repository.availableCars

        val filtered = if (query.isBlank()) {
            cars
        } else {
            cars.filter { car ->
                car.name.contains(query, ignoreCase = true) ||
                car.model.contains(query, ignoreCase = true)
            }
        }

        val sorted = applySorting(filtered)
        _filteredSortedList.value = sorted
        _currentIndex.value = 0
        updateCurrentCar()
    }

    /**
     * Sort cars
     */
    fun sort(by: SortType) {
        _sortType.value = by
        val cars = _filteredSortedList.value ?: emptyList()
        val sorted = applySorting(cars)
        _filteredSortedList.value = sorted
        _currentIndex.value = 0
        updateCurrentCar()
    }

    /**
     * Apply sorting logic
     */
    private fun applySorting(cars: List<Car>): List<Car> {
        return when (_sortType.value) {
            SortType.RATING -> cars.sortedByDescending { it.rating }
            SortType.YEAR -> cars.sortedByDescending { it.year }
            SortType.COST -> cars.sortedBy { it.dailyCost }
            else -> cars
        }
    }

    /**
     * Next car
     */
    fun next() {
        val cars = _filteredSortedList.value ?: emptyList()
        if (cars.isNotEmpty()) {
            val currentIndex = _currentIndex.value ?: 0
            val nextIndex = (currentIndex + 1) % cars.size
            _currentIndex.value = nextIndex
            updateCurrentCar()
        }
    }

    /**
     * Toggle favorite status
     */
    fun favoriteToggle(carId: String) {
        val isAdded = repository.toggleFavorite(carId)
        updateFavoriteCars()

        val car = repository.getCarById(carId)
        val message = if (isAdded) {
            "Added ${car?.name} to favorites"
        } else {
            "Removed ${car?.name} from favorites"
        }
        _message.value = message
    }

    /**
     * Rent car
     */
    fun rentCar(car: Car, days: Int): Boolean {
        val totalCost = car.dailyCost * days
        val currentBalance = _balance.value ?: 0

        // Check if balance is sufficient
        if (totalCost > currentBalance) {
            _message.value = "Insufficient credits"
            return false
        }

        // Check rental limit
        if (totalCost > 400) {
            _message.value = "Single rental limit: Cannot exceed 400 credits"
            return false
        }

        // Execute rental
        val rentalRecord = repository.rent(car.id, days)
        if (rentalRecord != null) {
            _balance.value = currentBalance - totalCost
            loadCars()
            updateCurrentCar()
            updateFavoriteCars()

            _message.value = "Booked ${car.name} for $days days, -$totalCost credits"
            return true
        } else {
            _message.value = "This car is already rented"
            return false
        }
    }

    /**
     * Toggle theme
     */
    fun toggleTheme() {
        val currentTheme = _isDarkTheme.value ?: false
        _isDarkTheme.value = !currentTheme
    }

    /**
     * Clear message
     */
    fun clearMessage() {
        _message.value = null
    }

    /**
     * Sort type enum
     */
    enum class SortType {
        RATING, YEAR, COST
    }
}

