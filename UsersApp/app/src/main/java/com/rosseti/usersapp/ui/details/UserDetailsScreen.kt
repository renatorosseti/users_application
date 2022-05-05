package com.rosseti.usersapp.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rosseti.usersapp.R
import com.rosseti.usersapp.domain.entity.UserEntity
import kotlinx.coroutines.launch

@Composable
fun UserDetailsScreen(
    navController: NavController,
    viewModel: UserDetailsViewModel,
    userId: String
) {
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
                        UserDetails(userAction.data, viewModel = viewModel)
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
fun UserDetails(
    user: UserEntity,
    viewModel: UserDetailsViewModel
) {
    var userName by remember { mutableStateOf(user.name) }

    var userBiography by remember { mutableStateOf(user.biography) }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(user.image)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = stringResource(R.string.app_name),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4F)
    )
    TextField(
        value = userName,
        label = { Text("Name") },
        onValueChange = {
            userName = it

        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp)
    )
    TextField(
        value = userBiography,
        label = { Text("Biography") },
        onValueChange = {
            userBiography = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 4.dp)
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        enabled = userName.isNotEmpty() && userBiography.isNotEmpty(),
        onClick = {
            viewModel.updateUser(
                userId = user.id,
                name = userName,
                biography = userBiography
            )

        }) {
        Text("Save profile")
    }
}