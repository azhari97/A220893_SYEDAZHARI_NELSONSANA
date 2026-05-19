package com.example.cupcake.data

import com.example.cupcake.data.local.OrderDao
import com.example.cupcake.data.local.OrderEntity
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val orderDao: OrderDao) {
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
    suspend fun insert(order: OrderEntity) {
        orderDao.insert(order)
    }
    suspend fun delete(order: OrderEntity) {
        orderDao.delete(order)
    }

}
