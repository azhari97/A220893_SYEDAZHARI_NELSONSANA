package com.example.a220893_nelson_lab2.ui.screens.products

import com.example.a220893_nelson_lab2.ui.screens.navigation.*

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.a220893_nelson_lab2.ui.screens.navigation.TopBar
import com.example.a220893_nelson_lab2.ui.screens.searchbar.SearchBar
import com.example.a220893_nelson_lab2.ui.components.sectiontitle.*

@Composable
fun ExploreScreen(modifier: Modifier = Modifier,navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {TopBar()},

    ) {
     paddingValues -> LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
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
        item{ProductGrid(searchQuery, navController)}

    }
    }
}