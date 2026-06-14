package com.example.a220893_nelson_lab2.data.local.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val itemId:String,
    val sellerId:Int = 0,
    val buyerId:Int = 0,
    val dealMethod:String,
    val paymentAttachmentUrl:String,
    val finalPrice:Double,
    val meetLocation:String,
    val extraDetails:String,
    val status:Int

)