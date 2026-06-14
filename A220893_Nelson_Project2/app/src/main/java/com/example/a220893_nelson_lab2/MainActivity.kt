package com.example.a220893_nelson_lab2
//screens
import com.example.a220893_nelson_lab2.ui.screens.home.*
import com.example.a220893_nelson_lab2.ui.screens.profile.*
import com.example.a220893_nelson_lab2.ui.screens.products.*
//viewmodels
import com.example.a220893_nelson_lab2.data.viewmodels.ProfileViewModel
import com.example.a220893_nelson_lab2.data.viewmodels.NavViewModel

//lib
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.a220893_nelson_lab2.ui.theme.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
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
import com.example.a220893_nelson_lab2.data.viewmodels.CartViewModel
import com.example.a220893_nelson_lab2.data.viewmodels.NewsViewModel
import com.example.a220893_nelson_lab2.data.viewmodels.ProductViewModel
import com.example.a220893_nelson_lab2.data.viewmodels.UserViewModel
import com.example.a220893_nelson_lab2.ui.screens.auth.LoginScreen
import com.example.a220893_nelson_lab2.ui.screens.news.NewsScreen

class MainActivity : ComponentActivity() {
    private val viewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                val user = viewModel.currentUser.value
                if (user == null) {
                    LoginScreen(
                        errorMessage = viewModel.errorMessage.value,
                        onRegisterClick = { name, email -> viewModel.register(name, email) },
                        onLoginClick = { email -> viewModel.login(email) }
                    )
                } else {
                    MainScreen(
                        userViewModel = viewModel,
                        onLogoutClick = { viewModel.logout() },
                    )
                }
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
fun MainScreen(
    userViewModel: UserViewModel,
    onLogoutClick: () -> Unit) {
    val navController = rememberNavController()
    val viewModel: ProfileViewModel = viewModel()
    val navViewModel: NavViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
//    val userViewModel: UserViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val newsViewModel: NewsViewModel = viewModel()
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
                                launchSingleTop = true
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
                composable("home") { HomeScreen(Modifier,navController,productViewModel,newsViewModel) }
                composable("home/news") { NewsScreen(newsViewModel,navController) }
                composable("explore") { ExploreScreen(Modifier,productViewModel,userViewModel,navController) }
                composable("explore/productdetails/{id}") { backStackEntry ->
                    val productId =
                        backStackEntry.arguments?.getString("id")
                    ProductDetailsScreen(
                        navController = navController,
                        viewModel = productViewModel,
                        productId = productId.toString(),
                        cartViewModel = cartViewModel,
                        userViewModel = userViewModel
                    )
                }
                composable("explore/addproduct") {AddProductScreen(productViewModel, userViewModel,navController) }
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