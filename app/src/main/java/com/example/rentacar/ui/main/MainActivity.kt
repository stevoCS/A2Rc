package com.example.rentacar.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentacar.R
import com.example.rentacar.databinding.ActivityMainBinding
import com.example.rentacar.model.Car
import com.example.rentacar.ui.adapter.FavoritesAdapter
import com.example.rentacar.ui.detail.DetailActivity
import com.example.rentacar.util.showSnackbar

/**
 * Main Activity, displays car list and search functionality
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter

    private val detailActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Rental successful, handle returned rental record
            val rentalRecord = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra("rental_record", com.example.rentacar.model.RentalRecord::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.getParcelableExtra("rental_record")
            }

            if (rentalRecord != null) {
                val car = viewModel.currentCar.value
                if (car != null) {
                    val success = viewModel.rentCar(car, rentalRecord.days)
                    if (success) {
                        binding.root.showSnackbar("Rental completed successfully!")
                    }
                }
            }
        } else {
            // Rental cancelled
            binding.root.showSnackbar(getString(R.string.rental_cancelled))
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply saved theme
        applySavedTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupObservers()
    }

    private fun applySavedTheme() {
        val sharedPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isDark = sharedPrefs.getBoolean("is_dark_theme", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }


    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter { car ->
            // Click favorite car, navigate to detail page
            openDetailActivity(car)
        }

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = favoritesAdapter
        }
    }

    private fun setupClickListeners() {
        // Theme toggle
        binding.themeSwitch.setOnCheckedChangeListener { _, _ ->
            viewModel.toggleTheme()
        }

        // Search functionality
        binding.searchEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.search(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // Favorite button
        binding.favoriteButton.setOnClickListener {
            val car = viewModel.currentCar.value
            if (car != null) {
                viewModel.favoriteToggle(car.id)
            }
        }

        // Rent button
        binding.rentButton.setOnClickListener {
            val car = viewModel.currentCar.value
            if (car != null) {
                openDetailActivity(car)
            }
        }

        // Next car button
        binding.nextButton.setOnClickListener {
            viewModel.next()
        }

        // Sort button
        binding.sortButton.setOnClickListener {
            showSortMenu()
        }
    }

    private fun setupObservers() {
        // Balance observer
        viewModel.balance.observe(this, Observer { balance ->
            binding.balanceChip.text = getString(R.string.balance_label, balance)
        })

        // Current car observer
        viewModel.currentCar.observe(this, Observer { car ->
            if (car != null) {
                displayCar(car)
                binding.emptyStateText.visibility = View.GONE
            } else {
                binding.emptyStateText.visibility = View.VISIBLE
            }
        })

        // Favorite cars observer
        viewModel.favoriteCars.observe(this, Observer { cars ->
            favoritesAdapter.updateCars(cars)
            // Update favorite button state for current car
            updateFavoriteButton()
        })

        // Theme observer
        viewModel.isDarkTheme.observe(this, Observer { isDark ->
            // Update switch text
            binding.themeSwitch.text = if (isDark) getString(R.string.dark_theme) else getString(R.string.light_theme)

            // Update switch state (without triggering listener)
            binding.themeSwitch.setOnCheckedChangeListener(null)
            binding.themeSwitch.isChecked = isDark
            binding.themeSwitch.setOnCheckedChangeListener { _, _ ->
                viewModel.toggleTheme()
            }

            // Save theme settings
            val sharedPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            sharedPrefs.edit().putBoolean("is_dark_theme", isDark).apply()

            // Apply theme
            val newMode = if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            if (AppCompatDelegate.getDefaultNightMode() != newMode) {
                AppCompatDelegate.setDefaultNightMode(newMode)
            }
        })

        // Message observer
        viewModel.message.observe(this, Observer { message ->
            message?.let {
                binding.root.showSnackbar(it)
                viewModel.clearMessage()
            }
        })
    }

    private fun displayCar(car: Car) {
        binding.carImage.setImageResource(car.imageRes)
        binding.carName.text = car.getDisplayName()
        binding.carModelYear.text = car.getYearText()
        binding.carKm.text = car.getKmText()
        binding.carDailyCost.text = car.getDailyCostText()
        binding.carRating.rating = car.rating
        binding.carRatingText.text = car.getRatingText()

        // Update favorite button state
        updateFavoriteButton()
    }

    private fun updateFavoriteButton() {
        val car = viewModel.currentCar.value ?: return
        val isFavorite = viewModel.favoriteCars.value?.any { it.id == car.id } ?: false
        // Toggle star icon based on favorite status (filled or border)
        binding.favoriteButton.setImageResource(
            if (isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_border
        )
    }
    
    private fun openDetailActivity(car: Car) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("car", car)
        }
        detailActivityLauncher.launch(intent)
    }
    
    private fun showSortMenu() {
        val sortTypes = arrayOf(
            getString(R.string.sort_by_rating ),
            getString(R.string.sort_by_year),
            getString(R.string.sort_by_cost)
        )
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Sort by")
            .setItems(sortTypes) { _, which ->
                val sortType = when (which) {
                    0 -> MainViewModel.SortType.RATING
                    1 -> MainViewModel.SortType.YEAR
                    2 -> MainViewModel.SortType.COST
                    else -> MainViewModel.SortType.RATING
                }
                viewModel.sort(sortType)
            }
            .show()
    }
}

