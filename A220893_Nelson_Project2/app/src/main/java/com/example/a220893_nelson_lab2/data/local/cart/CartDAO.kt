package com.example.a220893_nelson_lab2.data.local.cart

import androidx.room.*
import com.example.a220893_nelson_lab2.data.local.cart.CartEntity

@Dao
interface CartDao {
    @Insert
    suspend fun insertCart(
        cart: CartEntity
    )
    @Query(
        "SELECT * FROM cart"
    )
    suspend fun getCart():
            List<CartEntity>
    @Update
    suspend fun updateCart(
        cart: CartEntity
    )
    @Delete
    suspend fun deleteCart(
        cart: CartEntity
    )


}