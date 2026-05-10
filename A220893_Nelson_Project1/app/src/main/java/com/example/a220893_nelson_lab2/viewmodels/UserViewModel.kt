package com.example.a220893_nelson_lab2.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class User(
    val id: Int,
    val name: String,
    val email: String,
    )

data class UserUi(
    val name: String,
    val email: String
)

class UserViewModel : ViewModel() {
    private val _user = mutableStateListOf(
    User(1,"Ali","ali@mail.com")
    )

    val user: List<User> = _user

    fun getUser(userId: Int): UserUi? {
        return _user.find { it.id == userId }?.let {
            UserUi(
                name = it.name,
                email = it.email
            )
        }
    }
}