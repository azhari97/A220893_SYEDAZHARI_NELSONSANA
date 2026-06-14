package com.example.a220893_nelson_lab2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a220893_nelson_lab2.data.local.cart.CartDao
import com.example.a220893_nelson_lab2.data.local.cart.CartEntity
import com.example.a220893_nelson_lab2.data.local.user.UserDao
import com.example.a220893_nelson_lab2.data.local.user.UserEntity

@Database(entities = [UserEntity::class],
    version = 1,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun profileDao(): UserDao
    abstract fun cartDao(): CartDao

}
