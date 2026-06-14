package com.example.a220893_nelson_lab2.data.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
data class CartItem(
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

class CartViewModel : ViewModel() {
    private val _cartItem = mutableStateListOf<CartItem>(
    )

    val cartItems: List<CartItem> = _cartItem

    fun getCartItemById(id: Int): CartItem?{
        return _cartItem.find{ it.id.equals(id)}
    }
    fun addToCart(
        product: Product,
        offeredPrice: Double
    ) {
        val finalPrice = if (product.transactionType == "Donate") {
            0.0
        } else {
            offeredPrice
        }
        val newCartItem = CartItem(
            id = _cartItem.size + 1,
            itemId = product.id,
            sellerId = product.ownerId,
            buyerId = 0,
            dealMethod = "Meetup",
            paymentAttachmentUrl = "",
            finalPrice = finalPrice,
            meetLocation = "",
            extraDetails = "",
            status = 0
        )

        _cartItem.add(newCartItem)
    }

    fun updateMeetLocation(
        cartId: Int,
        meetLocation: String,
        extraDesc:String
    ) {

        val index = _cartItem.indexOfFirst {
            it.id == cartId
        }

        if (index != -1) {
            _cartItem[index] = _cartItem[index].copy(
                meetLocation = meetLocation,
                extraDetails = extraDesc,
                status = 1
            )
        }
    }
    fun randomizeOfferStatus(
        cartId: Int,
        cartItem: CartItem
    ) {

        viewModelScope.launch {

            delay(8000)

            val index = _cartItem.indexOfFirst {
                it.id == cartId
            }

            if (index != -1) {
                var randomStatus = 2
                if(cartItem.status != 3){
                    randomStatus = listOf(2, 3).random()
                }
                _cartItem[index] = _cartItem[index].copy(
                    status = randomStatus
                )
            }
        }
    }

    fun updateOfferPrice(
        cartId: Int,
        newPrice: Double,
        cartItem: CartItem
    ) {

        val index = _cartItem.indexOfFirst {
            it.id == cartId
        }

        if (index != -1) {

            _cartItem[index] = _cartItem[index].copy(
                finalPrice = newPrice,
                status = 1
            )

            randomizeOfferStatus(cartId,cartItem)
        }
    }

    fun completeTransaction(
        cartId: Int
    ) {

        val index = _cartItem.indexOfFirst {
            it.id == cartId
        }

        if (index != -1) {

            _cartItem[index] = _cartItem[index].copy(
                status = 4
            )
        }
    }
}