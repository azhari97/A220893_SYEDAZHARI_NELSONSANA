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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.a220893_nelson_lab2.ui.components.emptyState.EmptyState1
import com.example.a220893_nelson_lab2.ui.components.inforow.InfoRow
import com.example.a220893_nelson_lab2.ui.components.sectiontitle.SectionTitle
import com.example.a220893_nelson_lab2.ui.screens.navigation.TopBar
import com.example.a220893_nelson_lab2.data.viewmodels.CartItem
import com.example.a220893_nelson_lab2.data.viewmodels.ProductViewModel
import com.example.a220893_nelson_lab2.data.viewmodels.UserViewModel

@Composable
fun CartListScreen(modifier: Modifier,navController: NavController,cartViewModel: CartViewModel,userViewModel: UserViewModel,productViewModel: ProductViewModel) {
    val cartItems = cartViewModel.inCartItems.value
    var selectedTab by rememberSaveable {
        mutableIntStateOf(0)
    }
    val tabs = listOf("Current Cart")
    val filteredItems = cartItems

    Scaffold(
        topBar = { TopBar()}
    ) { paddingValues ->
        val pad = paddingValues

        Column(
            modifier = modifier.fillMaxSize().padding(top=50.dp)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                        },
                        text = {
                            Text(text = title)
                        }
                    )
                }
            }
            if (filteredItems.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState1(
                        message = "Cart is empty",
                        icon = Icons.Default.ShoppingCart
                    )
                }

            }
            else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(12.dp),
                    modifier = modifier.fillMaxSize()
                ) {
                    items(cartItems) { cartItem ->

                        CartItemCard(
                            cartItem = cartItem,
                            userViewModel = userViewModel,
                            productViewModel = productViewModel,
                            cartViewModel = cartViewModel,
                            onStatusChange = { newStatus ->
                                selectedTab = when (newStatus) {
                                    1, 2, 3 -> 0
                                    4 -> 1
                                    else -> 0
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
    onStatusChange : (Int)->Unit // to change tabs, in cart and other than in cart status cartitems
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    var meetLocation by rememberSaveable {
        mutableStateOf(cartItem.meetLocation)
    }
    var extraDesc by rememberSaveable {
        mutableStateOf(cartItem.extraDetails)
    }

    var shippingFee by rememberSaveable {
        mutableStateOf(cartItem.shipFee.toString())
    }
    var offerPrice by rememberSaveable {
        mutableStateOf(cartItem.finalPrice.toString())
    }

    var seller = userViewModel.getUser(cartItem.sellerId)
    val product = productViewModel.getProductById(cartItem.itemId)
    val context = LocalContext.current

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    showDialog = true
                },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {

                        Text(
                            text = "Cart #${cartItem.id}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        product?.let { prod ->
                            Text(
                                text = prod.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    AssistChip(
                        onClick = {

                        },
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

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {

                        Text(
                            text = "Deal Method",
                            style = MaterialTheme.typography.labelSmall
                        )

                        Text(
                            text = if (cartItem.dealMethod.isEmpty())
                                "Not Set"
                            else
                                cartItem.dealMethod
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {

                        Text(
                            text = "Price",
                            style = MaterialTheme.typography.labelSmall
                        )

                        Text(
                            text = "RM ${cartItem.finalPrice}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        // Dialog Popup
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },

                title = {
                    Text("Cart Details")
                },

                text = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(1){
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                ),
                                shape = RoundedCornerShape(28.dp),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                    Image(
                                        painter = painterResource(
                                            id = productViewModel.getImageResId(context,product?.imgUrl.toString())
                                        ),
                                        contentDescription = product?.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                            .padding(16.dp)
                                    )
                            }
                            Spacer(modifier=Modifier.height(12.dp))
                            // Cart Information
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SectionTitle("Cart Information")
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
                                    HorizontalDivider()
                                    product?.let { prod ->
                                        InfoRow(
                                            title = "Item Name",
                                            value = prod.name
                                        )
                                        InfoRow(
                                            title = "Item Type",
                                            value = prod.type
                                        )
                                        InfoRow(
                                            title = "Item Condition",
                                            value = prod.condition
                                        )
                                    }
                                    if (cartItem.status == 0) {
                                        InfoRow(
                                            title = "Offered Price",
                                            value = "RM ${cartItem.finalPrice}"
                                        )

                                        OutlinedTextField(
                                            value = meetLocation,
                                            onValueChange = {
                                                meetLocation = it
                                            },
                                            label = {
                                                Text("Meet Location")
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(14.dp)
                                        )

                                        OutlinedTextField(
                                            value = extraDesc,
                                            onValueChange = {
                                                extraDesc = it
                                            },
                                            label = {
                                                Text("Meetup Details")
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(14.dp)
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = MaterialTheme.colorScheme.secondaryContainer
                                        ) {

                                            Text(
                                                text = "Please fill in meetup details before finalizing your offer.",
                                                modifier = Modifier.padding(12.dp),
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }

                                    // STATUS 3
                                    if (cartItem.status == 3) {

                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = MaterialTheme.colorScheme.errorContainer
                                        ) {

                                            Text(
                                                text = "Your previous offer was rejected.",
                                                modifier = Modifier.padding(12.dp),
                                                color = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                        }

                                        OutlinedTextField(
                                            value = offerPrice,
                                            onValueChange = {
                                                offerPrice = it
                                            },
                                            label = {
                                                Text("Update Offer Price")
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                    }

                                    // STATUS 1
                                    if (cartItem.status == 1) {

                                        InfoRow(
                                            title = "Meetup Location",
                                            value = cartItem.meetLocation
                                        )

                                        InfoRow(
                                            title = "Meetup Details",
                                            value = cartItem.extraDetails
                                        )

                                        InfoRow(
                                            title = "Offer Price",
                                            value = "RM ${cartItem.finalPrice}"
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = MaterialTheme.colorScheme.secondaryContainer
                                        ) {

                                            Text(
                                                text = "Waiting for seller approval.",
                                                modifier = Modifier.padding(12.dp)
                                            )
                                        }
                                    }

                                    // STATUS 2
                                    if (cartItem.status == 2) {

                                        InfoRow(
                                            title = "Meetup Location",
                                            value = cartItem.meetLocation
                                        )

                                        InfoRow(
                                            title = "Meetup Details",
                                            value = cartItem.extraDetails
                                        )

                                        InfoRow(
                                            title = "Accepted Price",
                                            value = "RM ${cartItem.finalPrice}"
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        ) {

                                            Text(
                                                text = "Seller accepted your offer. Complete transaction after meetup.",
                                                modifier = Modifier.padding(12.dp),
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }

                                    // STATUS 4
                                    if (cartItem.status == 4) {

                                        InfoRow(
                                            title = "Meetup Location",
                                            value = cartItem.meetLocation
                                        )

                                        InfoRow(
                                            title = "Meetup Details",
                                            value = cartItem.extraDetails
                                        )

                                        InfoRow(
                                            title = "Price Paid",
                                            value = "RM ${cartItem.finalPrice}"
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = MaterialTheme.colorScheme.tertiaryContainer
                                        ) {

                                            Text(
                                                text = "Transaction Completed Successfully",
                                                modifier = Modifier.padding(12.dp),
                                                color = MaterialTheme.colorScheme.onTertiaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier=Modifier.height(12.dp))
                            // Seller Section
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text("Seller Information")
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = seller?.name ?: "Unknown Seller",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${seller?.email}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        }



                },
                confirmButton = {
                    Button(
                        onClick = {
//                            if (cartItem.status == 0) {
//                                cartViewModel.updateMeetLocation(
//                                    cartId = cartItem.id,
//                                    meetLocation = meetLocation,
//                                    extraDesc = extraDesc
//                                    )
//                                cartViewModel.(
//                                    cartItem.id,
//                                    cartItem
//                                )
//                            }
                            if (
                                cartItem.status == 3
                            ) {

                                if (offerPrice.isNotEmpty()) {
                                    cartViewModel.updateOfferPrice(
                                        cartId = cartItem.id,
                                        newPrice = offerPrice.toDouble(),
                                        cartItem
                                    )
                                }

                            } else if (cartItem.status == 2) {
                                cartViewModel.completeTransaction(
                                    cartItem.id
                                )
//                                onStatusChange(4)
                            }
                            showDialog = false
                        }
                    ) {

                        Text(
                            when (cartItem.status) {
                                0 -> "Submit"
//                                1 -> "Update Offer"
                                2 -> "Complete Transactions"
                                3 -> "Retry Offer"
//                                4 -> "Complete"
                                else -> "Close"
                            }
                        )
                    }
                },
                dismissButton = {
                    if (cartItem.status in listOf(0,2,3)) {
                        TextButton(
                            onClick = {
                                showDialog = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            )
        }

}