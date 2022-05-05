package com.rosseti.usersapp.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rosseti.usersapp.R
import com.rosseti.usersapp.domain.entity.UserEntity

@Composable
fun UserDetailsScreen(
    navController: NavController,
    viewModel: UserDetailsViewModel
) {
    var biography by remember { mutableStateOf("") }

    val userAction = viewModel.userState.collectAsState().value

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "User profile") },
            navigationIcon = {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null,
                    tint = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
            }
        )
    }) {
        Surface(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column {
                when (userAction) {
                    is UserDetailsViewModel.HomeAction.Successful -> {
                        UserDetails(user = userAction.data)
                    }
                    is UserDetailsViewModel.HomeAction.Error -> {
                    }
                    is UserDetailsViewModel.HomeAction.Loading -> {
                    }
                }


            }
        }
    }
}

@Composable
fun UserDetails(user: UserEntity) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(user.image)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = stringResource(R.string.app_name),
        contentScale = ContentScale.Crop,
        modifier = Modifier.clip(CircleShape)
    )
    TextField(
        value = user.name,
        label = { Text("Name") },
        onValueChange = { user.name = it },
        modifier = Modifier.padding(start = 4.dp)
    )
    TextField(
        value = user.biography,
        label = { Text("Biography") },
        onValueChange = { user.biography = it },
        modifier = Modifier.padding(start = 4.dp)
    )
}