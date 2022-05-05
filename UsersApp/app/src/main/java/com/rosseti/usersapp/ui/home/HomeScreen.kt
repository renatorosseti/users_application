package com.rosseti.usersapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rosseti.usersapp.R
import com.rosseti.usersapp.domain.entity.UserEntity
import com.rosseti.usersapp.navigation.AppScreens

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val showDelete = remember {
        mutableStateOf(false)
    }

    val usersIdSelected = remember {
        mutableStateListOf<String>()
    }

    val homeAction = viewModel.homeState.collectAsState().value
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "Users application")
            },
            actions = {
                if (usersIdSelected.isNotEmpty()) {
                    IconButton(onClick = {
                        println("Delete click: ${usersIdSelected.toList()}")
                    }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete icon")
                    }
                }
            }
        )
    }) {
        Surface(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            LazyColumn {
                when (homeAction) {
                    is HomeViewModel.HomeAction.Successful -> {
                        val list = homeAction.data
                        items(list) {
                            EventRow(
                                user = it,
                                navController = navController,
                                viewModel = viewModel,
                                usersIdSelected = usersIdSelected,
                                showDelete = showDelete
                            )
                        }
                    }
                    is HomeViewModel.HomeAction.Error -> {
                    }
                    is HomeViewModel.HomeAction.Loading -> {
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventRow(
    user: UserEntity,
    navController: NavController,
    viewModel: HomeViewModel,
    usersIdSelected: MutableList<String>,
    showDelete: MutableState<Boolean>
) {
    Surface(
        Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        color = if (usersIdSelected.contains(user.id)) Color(0xFFB2DFAB) else Color(0xFFB2fFFF)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        if (usersIdSelected.contains(user.id)) {
                            usersIdSelected.remove(user.id)
                        }
                        navController.navigate(route = AppScreens.UserDetailsScreen.name+"/${user.id}")
                        println("Single Click")
                    },
                    onLongClick = {
                        if (usersIdSelected.contains(user.id)) {
                            usersIdSelected.remove(user.id)
                        } else {
                            usersIdSelected.add(user.id)
                        }
                        showDelete.value = true
                        println("Long Click")
                    },
                ),
        ) {
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
            Text(text = user.name, modifier = Modifier.padding(start = 4.dp))
        }
    }
}
