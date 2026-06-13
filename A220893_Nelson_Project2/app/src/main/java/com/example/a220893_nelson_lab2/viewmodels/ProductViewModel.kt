package com.example.a220893_nelson_lab2.viewmodels

import android.content.Context
import com.example.a220893_nelson_lab2.R
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
data class Product(
    val id: Int,
    val name: String,
    val type: String,
    val transactionType:String,
    val imgUrl:String,
    val price: Double,
    val description:String,
    val condition:String,
    val ownerId: Int,

    )

class ProductViewModel : ViewModel() {
    private val _products = mutableStateListOf(
        Product(1, "Phone", "Electronics", "Sell", "phone", 110.0, "Smartphone still smooth performance and clear camera.","Used",1),
        Product(2, "Laptop", "Electronics", "Sell", "laptop", 1200.0, "Still useful laptop suitable for work and gaming.","Used",1),
        Product(3, "Shoes", "Fashion", "Sell", "shoes", 40.0, "Comfortable casual shoes,used for daily wear.","Used",1),
        Product(4, "Bag", "Fashion", "Sell", "bag", 12.0, "Stylish bag with enough space for essentials.","Used",1),
        Product(5, "Watch", "Accessories", "Sell", "watch", 10.0, "Elegant wrist watch with modern design.","Used",1),
        Product(6, "Book", "Education", "Donate", "book", 0.0, "Educational book for learning and self-improvement.","Used",1),
        Product(7, "Keyboard", "Electronics", "Sell", "keyboard", 30.0, "Secondhand mechanical keyboard with responsive keys.","Used",1),
        Product(8, "Mouse", "Electronics", "Sell", "mouse", 20.0, "Secondhand Wireless mouse with smooth tracking.","Used",1)
    )

    val products: List<Product> = _products

    fun addProduct(product: Product) {
        _products.add(product)
    }
    fun getImageResId(context: Context,imgUrl:String): Int {
        return context.resources.getIdentifier(
            imgUrl,
            "drawable",
            context.packageName
        ).takeIf { it != 0 }
            ?: R.drawable.justsharestufflogo
    }
    fun getProductById(id: Int): Product?{
        return _products.find{ it.id == id}
    }
    fun getProductsByUser(userId: Int): List<Product> {
        return _products.filter { it.ownerId == userId }
    }
}