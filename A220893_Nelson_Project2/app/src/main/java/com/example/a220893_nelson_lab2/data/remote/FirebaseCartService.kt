package com.example.a220893_nelson_lab2.data.remote

import android.util.Log
import com.example.a220893_nelson_lab2.data.viewmodels.CartItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FirebaseCartService {
    private val firestore = FirebaseFirestore.getInstance()
    private val cartCollection = firestore.collection("carts")

    suspend fun getCartItemsForBuyer(buyerEmail: String): List<CartItem> {
        return try {
            cartCollection
                .whereEqualTo("buyerId", buyerEmail.lowercase().trim())
                .get()
                .await()
                .toObjects(CartItem::class.java)
        } catch (e: Exception) {
            android.util.Log.e("FIREBASE_CART", "Error fetching user cart records", e)
            emptyList()
        }
    }

    suspend fun addCartItem(cartItem: CartItem) {
        val freshDocRef = cartCollection.document()
        val finalizedItem = cartItem.copy(id = freshDocRef.id)
        freshDocRef.set(finalizedItem).await()
    }


    suspend fun updateCartItem(cartItem: CartItem) {
        if (cartItem.id.isBlank()) return
        cartCollection
            .document(cartItem.id)
            .set(cartItem)
            .await()
    }
}