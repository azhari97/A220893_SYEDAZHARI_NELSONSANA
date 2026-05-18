package com.example.cupcake

import android.app.Application
import com.example.cupcake.data.OrderRepository
import com.example.cupcake.data.local.OrderDatabase

class CupcakeApplication : Application() {
    /**
     * App container instance used by the rest of the classes to get dependencies
     */
    val database: OrderDatabase by lazy { OrderDatabase.getDatabase(this) }
    val repository: OrderRepository by lazy { OrderRepository(database.orderDao()) }
}
