package com.rosseti.usersapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.rosseti.usersapp.ui.theme.Dark
import com.rosseti.usersapp.ui.theme.Grey

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
                Text(text = stringResource(id = R.string.app_name))
            },
            actions = {
                if (usersIdSelected.isNotEmpty()) {
                    IconButton(onClick = {
                        viewModel.deleteUsers(usersIdSelected.toList())

                    }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = stringResource(id = R.string.delete_icon))
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
            Column {
                LazyColumn {
                    when (homeAction) {
                        is HomeViewModel.HomeAction.Successful -> {
                            val list = homeAction.data
                            items(list) {
                                EventRow(
                                    user = it,
                                    navController = navController,
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
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .height(40.dp),
                    onClick = {
                        navController.navigate(route = AppScreens.UserDetailsScreen.name)
                    }) {
                    Text(stringResource(id = R.string.create_new_user))
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
    usersIdSelected: MutableList<String>,
    showDelete: MutableState<Boolean>
) {
    Surface(
        Modifier
            .padding(3.dp)
            .fillMaxWidth(),
    ) {
        Card(
            elevation = 2.dp,
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            backgroundColor = if (usersIdSelected.contains(user.id)) Grey else Dark,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(100.dp)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        if (usersIdSelected.isNotEmpty()) {
                            if (usersIdSelected.contains(user.id)) {
                                usersIdSelected.remove(user.id)
                            } else {
                                usersIdSelected.add(user.id)
                            }
                        } else {
                            navController.navigate(route = AppScreens.UserDetailsScreen.name + "/${user.id}")
                        }
                    },
                    onLongClick = {
                        if (usersIdSelected.contains(user.id)) {
                            usersIdSelected.remove(user.id)
                        } else {
                            usersIdSelected.add(user.id)
                        }
                        showDelete.value = true
                    },
                ),
        ) {
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.image)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(id = R.string.app_name),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = user.name,
                        color = Color.White
                    )
                }
            }
        }
    }
}
