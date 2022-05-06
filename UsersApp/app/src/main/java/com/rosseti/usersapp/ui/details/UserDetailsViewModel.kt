package com.rosseti.usersapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosseti.usersapp.domain.Resource
import com.rosseti.usersapp.domain.entity.UserEntity
import com.rosseti.usersapp.domain.usecase.CreateUserUseCase
import com.rosseti.usersapp.domain.usecase.GetUserByIdUseCase
import com.rosseti.usersapp.domain.usecase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val createUserUseCase: CreateUserUseCase
) :
    ViewModel() {

    private val userAction = MutableStateFlow<Resource<UserEntity>>(Resource.error())
    val userState = userAction.asStateFlow()

    fun fetchUsersById(userId: String) {
        viewModelScope.launch {
            getUserByIdUseCase(userId).collect {
                userAction.value = it
            }
        }
    }

    fun updateUser(userId: String, name: String, biography: String) {
        viewModelScope.launch {
            updateUserUseCase(userId, name, biography).collect {
                userAction.value = it
            }
        }
    }

    fun createUser(name: String, biography: String) {
        viewModelScope.launch {
            createUserUseCase(name, biography).collect {
                userAction.value = it
            }
        }
    }
}