package com.example.a220893_nelson_lab2.ui.screens.products

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a220893_nelson_lab2.ui.screens.navigation.*
import com.example.a220893_nelson_lab2.viewmodels.ProductViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a220893_nelson_lab2.R
import com.example.a220893_nelson_lab2.ui.theme.errorLight
import com.example.a220893_nelson_lab2.viewmodels.CartViewModel
import com.example.a220893_nelson_lab2.viewmodels.UserViewModel


@Composable
fun ProductDetailsScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    productId: Int
) {

    val product = viewModel.getProductById(productId)

    Scaffold(
        topBar = {
            TopBar()
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        product?.let { prod ->
            val context = LocalContext.current
            var offerPrice by rememberSaveable {
                mutableStateOf(prod.price.toString())
            }

            val owner = userViewModel.getUser(prod.ownerId)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {

                    Image(
                        painter = painterResource(
                            id = viewModel.getImageResId(context,prod.imgUrl)
                        ),
                        contentDescription = prod.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Product Information
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = prod.name,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = prod.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            AssistChip(
                                onClick = { },
                                label = {
                                    Text(prod.type)
                                }
                            )

                            AssistChip(
                                onClick = { },
                                label = {
                                    Text(prod.transactionType)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {

                            Text(
                                text = "RM ${prod.price}",
                                modifier = Modifier.padding(
                                    horizontal = 18.dp,
                                    vertical = 12.dp
                                ),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Seller Section
                owner?.let { something ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                text = "Seller Information",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(14.dp)
                                            .size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column {

                                    Text(
                                        text = something.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = something.email,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Offer Section
                            if (prod.transactionType != "Donate") {

                                OutlinedTextField(
                                    value = offerPrice,
                                    onValueChange = { input ->

                                        val filtered = input.filter {
                                            it.isDigit() || it == '.'
                                        }

                                        if (
                                            filtered.count { it == '.' } <= 1
                                        ) {
                                            offerPrice = filtered
                                        }
                                    },
                                    label = {
                                        Text("Offer Price")
                                    },
//                                    leadingIcon = {
//                                        Icon(
//                                            imageVector = Icons.AutoMirrored,
//                                            contentDescription = null
//                                        )
//                                    },
                                    supportingText = {
                                        if (offerPrice.isEmpty()) {
                                            Text("Price cannot be empty")
                                        }
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(18.dp)
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(58.dp),
                                    onClick = {

                                        if (offerPrice.isNotEmpty()) {

                                            cartViewModel.addToCart(
                                                product = prod,
                                                offeredPrice = offerPrice.toDouble()
                                            )
                                        }

                                        navController.navigate("cart")
                                    },
                                    shape = RoundedCornerShape(18.dp),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 6.dp,
                                        pressedElevation = 2.dp
                                    )
                                ) {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.Send,
                                            contentDescription = null
                                        )

                                        Text(
                                            text = "Send Offer",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            }

                            else {

                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(58.dp),
                                    onClick = {

                                        cartViewModel.addToCart(
                                            product = prod,
                                            offeredPrice = offerPrice.toDouble()
                                        )

                                        navController.navigate("cart")
                                    },
                                    shape = RoundedCornerShape(18.dp),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 6.dp
                                    )
                                ) {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = null
                                        )

                                        Text(
                                            text = "Request This Item",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedButton(
                                onClick = {
                                    navController.popBackStack()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(18.dp)
                            ) {

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text("Return Back")
                            }
                        }
                    }
                }
            }
        }
    }
}