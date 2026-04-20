package com.example.a220893_nelson_lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.a220893_nelson_lab2.ui.theme.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.rememberScrollState

import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel

import android.provider.ContactsContract.Profile
//nav ui
data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

data class Profile(
    val pImageUrl: String ="",
    val name: String = "",
    val email: String = ""
)

val navItems = listOf(
    NavItem("Home", Icons.Default.Home, "home"),
    NavItem("Explore", Icons.Default.Search, "explore"),
    NavItem("Chat", Icons.Default.Email, "chat"),
    NavItem("Profile", Icons.Default.Person, "profile")
)

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
                composable("home") { HomeScreen(Modifier) }
                composable("explore") { ExploreScreen(Modifier) }
                composable("chat") { ChatScreen(Modifier) }
                composable("profile") {
                    ProfileScreen(navController, viewModel)
                }
                composable("editprofile") {
                    EditProfileScreen(navController, viewModel)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            item { SectionTitle("Whats Happening?") }
            item{NewsCarouselSimple()}
//            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item { CategoryRow() }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item { SearchBar(
                searchText = searchText,
                onSearchChange = { searchText = it },
                onSearchClick = {
                    searchQuery = searchText // trigger filtering
                }
            ) }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            val isSearching = searchQuery.isNotEmpty()
//            val isSearchStr = isSearching.toString()
//            item{Text(isSearchStr)}
            if(!isSearching){
                item { SectionTitle("Recommended for you") }
            }else{
                item { SectionTitle("Search Result") }
            }
            item { ProductGrid(searchQuery) }
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(R.drawable.justsharestufflogo),
            contentDescription = "App Logo",
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "JustShareStuff",
            style = typography.titleLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications"
            )
        }
//        IconButton(onClick = {}) {
//            Icon(
//                imageVector = Icons.Default.Person,
//                contentDescription = "Login"
//            )
//        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = searchText,
            onValueChange = onSearchChange,
            placeholder = { Text("Search items...") },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
//            shape = RoundedCornerShape(28.dp),
//            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onSearchClick,
            modifier = Modifier
                .size(50.dp)
                .background(
                    colorScheme.primary,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.White
            )
        }
    }
}

@Composable
fun SectionTitle(text:String){
    Text(text=text, modifier = Modifier.padding(12.dp))
}

@Composable
fun CategoryRow() {
    val categories = listOf("Donation", "Sell", "Barter", "More...")
    // only shows whats on viewport,lazyload
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            AssistChip(
                onClick = {},
                label = { Text(category) },
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun ProductGrid(searchQuery: String) {
    val products = listOf(
        "Phone", "Laptop", "Shoes", "Bag",
        "Watch", "Book", "Keyboard", "Mouse"
    )

    val filteredProducts = if (searchQuery.isEmpty()) {
        products
    } else {
        products.filter {
            it.contains(searchQuery, ignoreCase = true)
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        modifier = Modifier.height(600.dp)
    ) {
        if (searchQuery.isEmpty()) {
            items(products.size) { index ->
                ProductCard(products[index])
            }
        }else{
            items(filteredProducts.size) { index ->
                ProductCard(filteredProducts[index])
            }
        }
    }
}

@Composable
fun ProductCard(name: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded } // toggle expand
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            Row {
                Image(
                    painter = painterResource(R.drawable.justsharestufflogo),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )

                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(text = name)
                    Text(
                        text = "Electronics",
                        style = typography.bodyMedium
                    )
                    Text(
                        text = "Sell",
                        style = typography.bodySmall
                    )
                    Text(
                        text = "RM0",
                        style = typography.headlineSmall
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "This is a sample description of the item. It appears when the card is expanded.",
                    style = typography.bodySmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Condition: Used",
                    style = typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun NewsCarouselSimple() {

    val newsList = listOf(
        R.drawable.justsharestufflogo,
        R.drawable.justsharestufflogo,
        R.drawable.justsharestufflogo,
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

// other pages
@Composable
fun ExploreScreen(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item{SectionTitle("Explore Items")}
        item{Spacer(modifier = Modifier.height(12.dp)) }
        item { SearchBar(
            searchText = searchText,
            onSearchChange = { searchText = it },
            onSearchClick = {
                searchQuery = searchText // trigger filtering
            }
        ) }
        item{ProductGrid(searchQuery)}

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
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel) {

    val profile = viewModel.profile.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Profile Image
        Image(
            painter = painterResource(R.drawable.justsharestufflogo),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(12.dp))
        Spacer(modifier = Modifier.height(16.dp))

        // Name
        Text(
            text = profile.name.ifEmpty { "Your Name" },
            style = typography.titleLarge,
            color = colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = profile.email.ifEmpty { "your@email.com" },
            style = typography.bodyMedium,
            color = colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate("editprofile") },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Edit Profile")
        }
    }
}
@Composable
fun EditProfileScreen(navController: NavController, viewModel: ProfileViewModel) {
    val profile = viewModel.profile.value
    var name by remember { mutableStateOf(profile.name) }
    var email by remember { mutableStateOf(profile.email) }
    Column(modifier = Modifier
        .padding(12.dp)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        SectionTitle("Edit Profile")
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Spacer(Modifier.padding(12.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(Modifier.padding(12.dp))
        Row() {
            Button(onClick = {
                navController.popBackStack()
            },
                colors= ButtonDefaults.buttonColors(errorLight),
                modifier = Modifier.padding(end = 6.dp)) {
                Text("Cancel")
            }
            Button(onClick = {
                viewModel.updateProfile(name, email)
                navController.popBackStack()
            }) {
                Text("Save Profile")
            }
        }
    }
}


@Composable
fun LoginScreen() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        // 🔷 Logo
        Image(
            painter = painterResource(R.drawable.justsharestufflogo),
            contentDescription = "App Logo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔷 Title
        Text(
            text = "JustShareStuff",
            style = typography.titleLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 🔷 Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔷 Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔷 Forgot Password
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Forgot Password?",
                style = typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔷 Login Button
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔷 Divider
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f))
            Text(
                text = "  OR  ",
                style = typography.bodySmall
            )
            Divider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔷 Sign Up Row
        Row {
            Text("Don’t have an account? ")
            Text(
                text = "Sign Up",
                color = colorScheme.primary
            )
        }
    }
}