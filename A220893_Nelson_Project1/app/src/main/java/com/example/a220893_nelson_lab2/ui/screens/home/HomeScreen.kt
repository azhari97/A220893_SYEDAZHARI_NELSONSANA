package com.example.a220893_nelson_lab2.ui.screens.home

import com.example.a220893_nelson_lab2.ui.screens.searchbar.*
import com.example.a220893_nelson_lab2.ui.components.inforow.CategoryRow

import com.example.a220893_nelson_lab2.viewmodels.ProductViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.a220893_nelson_lab2.NewsCarouselSimple
import com.example.a220893_nelson_lab2.ui.screens.products.*
import com.example.a220893_nelson_lab2.R
import com.example.a220893_nelson_lab2.ui.components.inforow.CategoryRow
import com.example.a220893_nelson_lab2.ui.components.sectiontitle.SectionTitle
import com.example.a220893_nelson_lab2.ui.screens.navigation.TopBar
import com.example.a220893_nelson_lab2.viewmodels.Product

@Composable
fun HomeScreen(modifier: Modifier = Modifier,navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBar()
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            item { SectionTitle("Whats Happening?") }
            item { NewsCarouselSimple() }
//            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item { CategoryRow(  searchOn = { selectedCategory ->
                searchText = selectedCategory
                searchQuery = selectedCategory
            })}
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                SearchBar(
                    searchText = searchText,
                    onSearchChange = { searchText = it },
                    onSearchClick = {
                        searchQuery = searchText // trigger filtering
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            val isSearching = searchQuery.isNotEmpty()
//            val isSearchStr = isSearching.toString()
//            item{Text(isSearchStr)}
            if (!isSearching) {
                item { SectionTitle("Recommended for you") }
            } else {
                item { SectionTitle("Search Result") }
            }

            item { ProductGrid(searchQuery, navController) }
        }
    }
}

//@Composable
//fun TopBar() {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        Image(
//            painter = painterResource(R.drawable.justsharestufflogo),
//            contentDescription = "App Logo",
//            modifier = Modifier.size(40.dp)
//        )
//
//        Spacer(modifier = Modifier.width(8.dp))
//
//        Text(
//            text = "JustShareStuff",
//            style = typography.titleLarge
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        IconButton(onClick = {}) {
//            Icon(
//                imageVector = Icons.Default.Notifications,
//                contentDescription = "Notifications"
//            )
//        }
////        IconButton(onClick = {}) {
////            Icon(
////                imageVector = Icons.Default.Person,
////                contentDescription = "Login"
////            )
////        }
//    }
//}