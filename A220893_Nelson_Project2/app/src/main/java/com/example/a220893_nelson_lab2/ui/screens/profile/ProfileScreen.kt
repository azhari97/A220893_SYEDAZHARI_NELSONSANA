package com.example.a220893_nelson_lab2.ui.screens.profile

import com.example.a220893_nelson_lab2.viewmodels.ProfileViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a220893_nelson_lab2.R
import com.example.a220893_nelson_lab2.ui.components.sectiontitle.*
import com.example.a220893_nelson_lab2.ui.screens.navigation.TopBar
import com.example.a220893_nelson_lab2.ui.theme.errorLight
import kotlin.text.ifEmpty

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel) {
    val profile = viewModel.profile.value
    Scaffold(
        topBar = { TopBar() }
    ) {
        paddingValues ->
        val pad = paddingValues
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {

            // Profile Image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            ){
                Image(
                    painter = painterResource(R.drawable.justsharestufflogo),
                    contentDescription = "Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

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

}
@Composable
fun EditProfileScreen(navController: NavController, viewModel: ProfileViewModel) {
    val profile = viewModel.profile.value
//    var name by remember { mutableStateOf(editProfile.name) }
//    var email by remember { mutableStateOf(editProfile.email) }
    Column(modifier = Modifier
        .padding(12.dp)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        SectionTitle("Edit Profile")
        TextField(
            value = profile.name,
            onValueChange = { profile.name = it },
            label = { Text("Name") }
        )
        Spacer(Modifier.padding(12.dp))
        TextField(
            value = profile.email,
            onValueChange = { profile.email = it },
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
                viewModel.updateProfile(profile.name, profile.email)
                navController.popBackStack()
            }) {
                Text("Save Profile")
            }
        }
    }
}