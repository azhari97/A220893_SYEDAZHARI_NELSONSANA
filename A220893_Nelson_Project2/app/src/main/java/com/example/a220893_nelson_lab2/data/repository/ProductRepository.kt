package com.example.a220893_nelson_lab2.data.repository

import com.example.a220893_nelson_lab2.data.remote.FirebaseProductService
import com.example.a220893_nelson_lab2.data.viewmodels.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepository {
    private val firebaseService: FirebaseProductService = FirebaseProductService()

    suspend fun fetchActiveListings(): List<Product> = firebaseService.getProducts()

    suspend fun addNewListing(product: Product) = firebaseService.uploadProduct(product)

    suspend fun softDeleteListing(productId: String) = firebaseService.updateProductToUnlisted(productId)

    suspend fun permanentDeleteProduct(productId: String) = firebaseService.deleteProduct(productId)
}