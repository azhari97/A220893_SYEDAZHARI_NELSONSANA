package com.example.a220893_nelson_lab2.ui.screens.products

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.a220893_nelson_lab2.R
import com.example.a220893_nelson_lab2.ui.components.inforow.InfoRow
import com.example.a220893_nelson_lab2.viewmodels.Product
import com.example.a220893_nelson_lab2.viewmodels.ProductViewModel

@Composable
fun ProductGrid(searchQuery: String,navController: NavController
) {
    val productViewModel: ProductViewModel = viewModel()
    val products = productViewModel.products

    val filteredProducts = if (searchQuery.isEmpty()) {
        products
    } else {
        products.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.type.contains(searchQuery, ignoreCase = true) ||
                    it.transactionType.contains(searchQuery, ignoreCase = true)

        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        modifier = Modifier.height(600.dp)
//            .clip(RectangleShape)
//            .border(BorderStroke(2.dp, colorScheme.secondary),RectangleShape)
    ) {
        items(filteredProducts.size) { idx ->
            ProductCard(filteredProducts[idx],navController,productViewModel)
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    navController: NavController,
    productViewModel: ProductViewModel
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                expanded = !expanded
            }
            .animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    tonalElevation = 4.dp,

                ) {
                    val context = LocalContext.current
                    Image(
                        painter = painterResource(
                            id =  productViewModel.getImageResId(context,product.imgUrl)
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = product.type,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    AssistChip(
                        onClick = { },
                        label = {
                            Text(product.transactionType)
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "RM ${product.price}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(14.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    InfoRow(
                        title = "Description",
                        value = product.description
                    )

                    InfoRow(
                        title = "Condition",
                        value = product.condition
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        navController.navigate(
                            "productdetails/${product.id}"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Text(
                        text = "View More",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

//@Composable
//fun ProductCard(product: Product,navController: NavController
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable { expanded = !expanded } // toggle expand
//            .animateContentSize(),
////        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(modifier = Modifier.padding(8.dp)) {
//            Row {
//                Image(
//                    painter = painterResource(R.drawable.justsharestufflogo),
//                    contentDescription = null,
//                    modifier = Modifier.size(70.dp)
////                        .clip(CircleShape)
//                )
//
//                Column(
//                    modifier = Modifier.padding(start = 8.dp)
//                ) {
//                    Text(text = product.name)
//                    Text(
//                        text = product.type,
//                        style = typography.bodyMedium
//                    )
//                    Text(
//                        text = product.transactionType,
//                        style = typography.bodySmall
//                    )
//                    Text(
//                        text = "RM ${product.price}",
//                        style = typography.bodySmall
//                    )
//                }
//            }
//
//            if (expanded) {
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = product.description,
//                    style = typography.bodySmall
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    text = "Condition: ${product.condition}",
//                    style = typography.bodySmall
//                )
//                Spacer(modifier = Modifier.padding(vertical =3.dp))
//                Button(
//                    onClick = {
//                        navController.navigate("productdetails/${product.id}")
//                    },
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
//                ) {
//                    Text("View More")
//                }
//            }
//        }
//    }
//}

