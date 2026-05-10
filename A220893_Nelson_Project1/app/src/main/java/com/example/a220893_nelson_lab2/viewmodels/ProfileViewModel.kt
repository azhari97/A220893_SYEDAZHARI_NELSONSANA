package com.example.a220893_nelson_lab2.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class Profile(
    val pImageUrl: String ="",
    val name: String = "",
    val email: String = ""
)

class ProfileViewModel : ViewModel() {

    var profile = mutableStateOf(Profile())
        private set

    fun updateProfile(name: String, email: String) {
        profile.value = profile.value.copy(
            name = name,
            email = email
        )
    }
}