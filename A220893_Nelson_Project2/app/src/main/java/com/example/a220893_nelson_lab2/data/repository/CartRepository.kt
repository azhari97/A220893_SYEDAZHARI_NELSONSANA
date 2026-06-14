package com.example.a220893_nelson_lab2.data.repository

import com.example.a220893_nelson_lab2.data.viewmodels.CartItem
import com.example.a220893_nelson_lab2.data.remote.FirebaseCartService

class CartRepository(
    private val firebaseService: FirebaseCartService = FirebaseCartService()
) {
    suspend fun getItems(buyerEmail: String): List<CartItem> = firebaseService.getCartItemsForBuyer(buyerEmail)
    suspend fun saveNewItem(cartItem: CartItem) = firebaseService.addCartItem(cartItem)
    suspend fun updateExistingItem(cartItem: CartItem) = firebaseService.updateCartItem(cartItem)
}