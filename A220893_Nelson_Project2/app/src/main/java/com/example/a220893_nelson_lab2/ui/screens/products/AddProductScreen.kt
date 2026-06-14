package com.example.a220893_nelson_lab2.ui.screens.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.example.a220893_nelson_lab2.data.viewmodels.ProductViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.a220893_nelson_lab2.data.viewmodels.Product
import com.example.a220893_nelson_lab2.data.viewmodels.UserViewModel

@Composable
fun AddProductScreen(
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
    navController: NavController
){
    val user = userViewModel.getUser(1);
    val id: String = "0"
    var name by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf("Used") }
    var transactionType by rememberSaveable { mutableStateOf("Sell") }
    var imgUrl by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var condition by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable {
        mutableDoubleStateOf(0.0)
    }

    Column {
        OutlinedTextField(
            value=name,
            onValueChange={
                name=it
            },

            label={
                Text("Name")
            }

        )
        OutlinedTextField(
            value="ss",
            onValueChange={
            },

            label={
                Text("Price")
            }

        )

        Button(
            onClick={
                productViewModel.addProduct(
                    Product(
                        id = "0",
                        name = name,
                        price = price.toDouble(),
                        type = "Used",
                        transactionType = "Sell",
                        imgUrl = " ",
                        description = " ",
                        condition = " ",
                        ownerId = 1
                    )
                )
                navController.popBackStack()
            }){
            Text("Add Product")
        }
    }
}