package com.example.a220893_nelson_lab2
//screens
import com.example.a220893_nelson_lab2.ui.screens.home.*
import com.example.a220893_nelson_lab2.ui.screens.profile.*
import com.example.a220893_nelson_lab2.ui.screens.products.*
import com.example.a220893_nelson_lab2.ui.components.sectiontitle.*
//viewmodels
import com.example.a220893_nelson_lab2.viewmodels.ProfileViewModel
import com.example.a220893_nelson_lab2.viewmodels.NavViewModel

//lib
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.a220893_nelson_lab2.ui.theme.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.rememberScrollState

import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a220893_nelson_lab2.ui.screens.cart.CartListScreen
import com.example.a220893_nelson_lab2.viewmodels.CartViewModel
import com.example.a220893_nelson_lab2.viewmodels.ProductViewModel
import com.example.a220893_nelson_lab2.viewmodels.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val backStackEntry = navController.currentBackStackEntryAsState()
    return backStackEntry.value?.destination?.route
}
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: ProfileViewModel = viewModel()
    val navViewModel: NavViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val navItems = navViewModel.navItems

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute(navController) == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true //avoid duplicates routes stack
                            }
                        },
                        icon = {
                            Icon(item.icon, contentDescription = item.label)
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {

            NavHost(
                navController = navController,
                startDestination = "home",
            ) {
                composable("home") { HomeScreen(Modifier,navController) }
                composable("explore") { ExploreScreen(Modifier,navController) }
                composable("productdetails/{productId}") { backStackEntry ->
                    val productId =
                        backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                    ProductDetailsScreen(
                        navController = navController,
                        viewModel = productViewModel,
                        productId = productId,
                        cartViewModel = cartViewModel,
                        userViewModel = userViewModel
                    )
                }
                composable("profile") {
                    ProfileScreen(navController, viewModel)
                }
                composable("cart") { CartListScreen(Modifier,navController,cartViewModel, userViewModel, productViewModel) }
                composable("editprofile") {
                    EditProfileScreen(navController, viewModel)
                }
            }
        }
    }
}
@Composable
fun NewsCarouselSimple() {

    val newsList = listOf(
        R.drawable.justsharestufflogonews,
        R.drawable.justsharestufflogonews,
        R.drawable.justsharestufflogonews,
    )

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
        ) {

            newsList.forEach { imageRes ->

                Card(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .width(280.dp)
                        .height(160.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = "News",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Optional simple indicator (static)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(newsList.size) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
fun SellScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Chat Screen")
    }
}

@Composable
fun ChatScreen(modifier: Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Chat Screen")
    }
}