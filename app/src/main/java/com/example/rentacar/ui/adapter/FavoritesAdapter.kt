package com.example.rentacar.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rentacar.databinding.ItemFavoriteBinding
import com.example.rentacar.model.Car

/**
 * Favorites list adapter
 */
class FavoritesAdapter(
    private val onCarClick: (Car) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {
    
    private var cars = listOf<Car>()
    
    fun updateCars(newCars: List<Car>) {
        cars = newCars
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(cars[position])
    }
    
    override fun getItemCount(): Int = cars.size
    
    inner class FavoriteViewHolder(
        private val binding: ItemFavoriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(car: Car) {
            binding.favoriteCarImage.setImageResource(car.imageRes)
            binding.favoriteCarName.text = car.name
            binding.favoriteCarYear.text = car.year.toString()
            
            binding.root.setOnClickListener {
                onCarClick(car)
            }
        }
    }
}

