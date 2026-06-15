package com.example.a220893_nelson_lab2.data.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import androidx.compose.runtime.State
import com.example.a220893_nelson_lab2.data.repository.UserRepository
//import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.collectLatest

data class User(
    val email: String = "", //unique identifier
    val name: String = "",
    val pImageUrl: String = ""
)

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    //'?' valuechecking and to set this state bucket to be null when logged out
    private val _currentUser = mutableStateOf<User?>(null)
    val currentUser: State<User?> = _currentUser

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        viewModelScope.launch {
            repository.localUserFlow.collectLatest { user ->
                _currentUser.value = if (user?.email?.isNotBlank() == true) user else null
            }
        }
    }

    fun register(name: String, email: String) {
        viewModelScope.launch {
            try {
                repository.registerNewUser(name, email)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Registration Failed"
                Log.e("USER_VM","issue here",e)
            }
        }
    }

    fun login(email: String) {
        viewModelScope.launch {
            try {
                val success = repository.loginWithEmail(email)
                _errorMessage.value = if (success) null else "No profile matched that email"
            } catch (e: Exception) {
                _errorMessage.value = "Connection error"
                Log.e("USER_VM","issue here",e)

            }
        }
    }

    fun updateProfile(newName: String, currentEmail: String) {
        viewModelScope.launch {
            try {
                repository.updateCrudProfile(newName, currentEmail)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Update failed"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearLocalCacheAndLogout()
            _errorMessage.value = null
        }
    }
}