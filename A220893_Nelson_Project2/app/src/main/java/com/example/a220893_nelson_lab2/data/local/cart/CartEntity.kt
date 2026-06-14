package com.example.a220893_nelson_lab2.data.local.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String = "",
    val productId: String = "",
    val sellerId: String = "",
    val buyerId: String = "",
    val dealMethod: String = "",
    val finalPrice: Double = 0.0,
    val meetLocation: String = "",
    val extraDetails: String = "",

// Status Guardrails:
// 0 = In Cart (Draft)
// 1 = Pending Approval
// 2 = Accepted
// 3 = Rejected
// 4 = Complete
    val status: Int = 0

)