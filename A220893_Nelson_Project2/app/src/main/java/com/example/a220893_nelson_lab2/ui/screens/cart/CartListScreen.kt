package com.example.a220893_nelson_lab2.ui.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a220893_nelson_lab2.data.viewmodels.CartViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.a220893_nelson_lab2.ui.components.emptyState.EmptyState1
import com.example.a220893_nelson_lab2.ui.components.inforow.InfoRow
import com.example.a220893_nelson_lab2.ui.components.sectiontitle.SectionTitle
import com.example.a220893_nelson_lab2.ui.screens.navigation.TopBar
import com.example.a220893_nelson_lab2.data.viewmodels.CartItem
import com.example.a220893_nelson_lab2.data.viewmodels.ProductViewModel
import com.example.a220893_nelson_lab2.data.viewmodels.UserViewModel
import com.example.a220893_nelson_lab2.R


@Composable
fun CartListScreen(
    modifier: Modifier,
    navController: NavController,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    productViewModel: ProductViewModel
) {
    val currentUserEmail = userViewModel.currentUser.value?.email ?: ""

    LaunchedEffect(currentUserEmail) {
        if (currentUserEmail.isNotBlank()) {
            cartViewModel.loadAllUserRelatedCarts(currentUserEmail)
        }
    }

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Current Cart", "Offers & History")
    val filteredItems = when (selectedTab) {
        0 -> cartViewModel.inCartItems.value
        1 -> cartViewModel.transactionItems.value
        else -> emptyList()
    }

    Scaffold(
        topBar = { TopBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(text = title) }
                    )
                }
            }

            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState1(
                        message = if (selectedTab == 0) "Your cart is empty" else "No order history found",
                        icon = Icons.Default.ShoppingCart
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredItems) { cartItem ->
                        CartItemCard(
                            cartItem = cartItem,
                            userViewModel = userViewModel,
                            productViewModel = productViewModel,
                            cartViewModel = cartViewModel,
                            onStatusChange = { newStatus ->
                                selectedTab = when (newStatus) {
                                    0 -> 0
                                    else -> 1
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    userViewModel: UserViewModel,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    modifier: Modifier = Modifier,
    onStatusChange: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    var meetLocation by rememberSaveable { mutableStateOf(cartItem.meetLocation) }
    var extraDesc by rememberSaveable { mutableStateOf(cartItem.extraDetails) }
    var offerPrice by rememberSaveable { mutableStateOf(cartItem.finalPrice.toString()) }

    val currentUserEmail = userViewModel.currentUser.value?.email ?: ""

    val isSeller = currentUserEmail.lowercase().trim() == cartItem.sellerId.lowercase().trim()
    val product = productViewModel.getProductById(cartItem.productId)
    val sellerInfoId = if (isSeller) cartItem.buyerId else cartItem.sellerId

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isSeller) "Incoming Offer" else "My Purchase Request",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    // Safe Check for Product Name
                    if (product != null) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = "Loading Product Details...",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            when (cartItem.status) {
                                0 -> "In Cart"
                                1 -> "Awaiting Seller"
                                2 -> "Accepted"
                                3 -> "Rejected"
                                else -> "Completed"
                            }
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = if (isSeller) "Buyer" else "Seller", style = MaterialTheme.typography.labelSmall)
                    Text(text = sellerInfoId, style = MaterialTheme.typography.bodyMedium)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Offer Price", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = "RM ${cartItem.finalPrice}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(product?.name ?: "Offer Details") },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                            shape = RoundedCornerShape(28.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (product != null && product.imgUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = product.imgUrl,
                                    error = painterResource(R.drawable.justsharestufflogo),
                                    contentDescription = "Thumbnail for ${product.name}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(R.drawable.justsharestufflogo),
                                    contentDescription = "Fallback Logo",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(text = "Offer Details", style = MaterialTheme.typography.titleSmall)
                                HorizontalDivider()

                                when (cartItem.status) {
                                    0 -> {
                                        OutlinedTextField(
                                            value = meetLocation,
                                            onValueChange = { meetLocation = it },
                                            label = { Text("Meet Location") },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        OutlinedTextField(
                                            value = extraDesc,
                                            onValueChange = { extraDesc = it },
                                            label = { Text("Meetup Details") },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    1 -> {
                                        Text("Location: ${cartItem.meetLocation}", style = MaterialTheme.typography.bodyMedium)
                                        Text("Details: ${cartItem.extraDetails}", style = MaterialTheme.typography.bodyMedium)

                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (isSeller) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = if (isSeller) "Review the details below before accepting." else "Waiting for seller approval.",
                                                modifier = Modifier.padding(12.dp),
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                    2 -> {
                                        Text("Location: ${cartItem.meetLocation}")
                                        Text("Details: ${cartItem.extraDetails}")
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = if (isSeller) "Waiting for buyer to close transaction after meetup." else "Offer accepted! Meet up and click Complete below.",
                                                modifier = Modifier.padding(12.dp)
                                            )
                                        }
                                    }
                                    3 -> {
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.errorContainer,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = if (isSeller) "You rejected this offer." else "Your offer was rejected. Adjust price below to re-submit.",
                                                modifier = Modifier.padding(12.dp),
                                                color = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                        }
                                        if (!isSeller) {
                                            OutlinedTextField(
                                                value = offerPrice,
                                                onValueChange = { offerPrice = it },
                                                label = { Text("New Offer Price (RM)") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                    4 -> {
                                        Text("Location: ${cartItem.meetLocation}")
                                        Text("Finalized Price: RM ${cartItem.finalPrice}")
                                        Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.tertiaryContainer, modifier = Modifier.fillMaxWidth()) {
                                            Text(text = "Transaction Complete", modifier = Modifier.padding(12.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (isSeller && cartItem.status == 1) {
                    Row {
                        TextButton(
                            onClick = {
                                cartViewModel.respondToIncomingOffer(cartItem.id, isAccepted = false, currentUserEmail)
                                showDialog = false
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Reject Offer")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                cartViewModel.respondToIncomingOffer(cartItem.id, isAccepted = true, currentUserEmail)
                                showDialog = false
                            }
                        ) {
                            Text("Accept Offer")
                        }
                    }
                } else {
                    val buttonText = when (cartItem.status) {
                        0 -> "Submit Offer"
                        2 -> if (!isSeller) "Complete Transaction" else "Close"
                        3 -> "Retry Offer"
                        else -> "Close"
                    }

                    Button(
                        onClick = {
                            when (cartItem.status) {
                                0 -> {
                                    cartViewModel.updateMeetLocation(
                                        cartItem.id,
                                        meetLocation,
                                        extraDesc,
                                        currentUserEmail
                                    )
                                    onStatusChange(1)
                                }
                                3 -> if (offerPrice.isNotEmpty()) cartViewModel.updateOfferPrice(cartItem.id, offerPrice.toDouble(), currentUserEmail)
                                2 -> if (!isSeller) cartViewModel.completeTransaction(cartItem.id, currentUserEmail)
                            }
                            showDialog = false
                        }
                    ) {
                        Text(buttonText)
                    }
                }
            },
            dismissButton = {
                if (!(cartItem.status == 2 && !isSeller)) {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Close")
                    }
                }
            }
        )
    }
}