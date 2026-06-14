package com.example.a220893_nelson_lab2.data.viewmodels

import android.content.Context
import android.util.Log
import com.example.a220893_nelson_lab2.R
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a220893_nelson_lab2.data.repository.ProductRepository
import kotlinx.coroutines.launch

data class Product(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val transactionType: String = "Sell",
    val imgUrl: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val condition: String = "Used",
    val ownerId: String = "", //refer email
    val unlisted: Boolean = false
)

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _products = mutableStateListOf<Product>()
    val products: List<Product> = _products

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            try {
                val activeListings = repository.fetchActiveListings()
                _products.clear()
                _products.addAll(activeListings)
                Log.d("PRODUCT_VM", "Successfully synchronized ${activeListings.size} marketplace items.")
            } catch (e: Exception) {
                Log.e("PRODUCT_VM", "Failed to clear or pull cloud arrays", e)
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.addNewListing(product)
                loadProducts()
            } catch (e: Exception) {
                Log.e("PRODUCT_VM", "Could not upload new listing", e)
            }
        }
    }

    fun removeProductToUnlisted(productId: String) {
        viewModelScope.launch {
            try {
                repository.softDeleteListing(productId)
                loadProducts()
            } catch (e: Exception) {
                Log.e("PRODUCT_VM", "Error changing document status to hidden", e)
            }
        }
    }

    fun getProductById(id: String): Product? {
        return _products.find { it.id == id }
    }

    fun getProductsByUser(email: String): List<Product> {
        return _products.filter { it.ownerId.lowercase() == email.lowercase().trim() }
    }

//    fun getImageResId(context: Context, imgUrl: String): Int {
//        return context.resources.getIdentifier(
//            imgUrl,
//            "drawable",
//            context.packageName
//        ).takeIf { it != 0 } ?: R.drawable.justsharestufflogo
//    }
}