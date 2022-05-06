package com.rosseti.usersapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosseti.usersapp.domain.Resource
import com.rosseti.usersapp.domain.entity.UserEntity
import com.rosseti.usersapp.domain.usecase.DeleteUserUseCase
import com.rosseti.usersapp.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) :
    ViewModel() {

    private val homeAction = MutableStateFlow<Resource<List<UserEntity>>>(Resource.error())
    val homeState = homeAction.asStateFlow()

    fun fetchUsers() {
        viewModelScope.launch {
            getUsersUseCase().collect { resource ->
                homeAction.value = resource
            }
        }
    }

    fun deleteUsers(userIds: List<String>) {
        userIds.forEach {
            viewModelScope.launch {
                deleteUserUseCase(it)
            }
        }
        fetchUsers()
    }
}