package com.rosseti.usersapp.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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

@Composable
fun UserDetailsScreen(
    navController: NavController,
    viewModel: UserDetailsViewModel,
    userId: String = ""
) {
    val userAction = viewModel.userState.collectAsState().value

    var profileId by remember { mutableStateOf(userId) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.user_profile)) },
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
                        profileId = userAction.data.id
                        UserDetails(userAction.data, viewModel = viewModel)
                    }
                    is UserDetailsViewModel.HomeAction.Error -> {}
                    is UserDetailsViewModel.HomeAction.Loading -> {
                        CircularProgressIndicator(Modifier.align(CenterHorizontally).padding(top = 180.dp))
                    }
                }
                if (profileId.isNullOrBlank()) {
                    UserDetails(UserEntity(), viewModel = viewModel, isNewProfile = true)
                }
            }
        }
    }
}

@Composable
fun UserDetails(
    user: UserEntity,
    viewModel: UserDetailsViewModel,
    isNewProfile: Boolean = false
) {
    var userName by remember { mutableStateOf(user.name) }

    var userBiography by remember { mutableStateOf(user.biography) }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(user.image)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = stringResource(id = R.string.app_name),
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
            .padding(start = 8.dp, top = 20.dp)
    )
    TextField(
        value = userBiography,
        label = { Text("Biography") },
        onValueChange = {
            userBiography = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 20.dp)
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(40.dp),
        enabled = userName.isNotEmpty() && userBiography.isNotEmpty(),
        onClick = {
            if (isNewProfile) {
                viewModel.createUser(
                    name = userName,
                    biography = userBiography
                )
            } else {
                viewModel.updateUser(
                    userId = user.id,
                    name = userName,
                    biography = userBiography
                )
            }
        }) {
        Text(stringResource(id = R.string.save_profile))
    }
}