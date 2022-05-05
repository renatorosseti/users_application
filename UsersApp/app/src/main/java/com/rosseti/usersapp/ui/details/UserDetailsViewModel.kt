package com.rosseti.usersapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosseti.usersapp.domain.Resource
import com.rosseti.usersapp.domain.entity.UserEntity
import com.rosseti.usersapp.domain.usecase.GetUserByIdUseCase
import com.rosseti.usersapp.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(private val getUserByIdUseCase: GetUserByIdUseCase) :
    ViewModel() {

    sealed class HomeAction {
        object Loading : HomeAction()
        object Error : HomeAction()
        data class Successful(val data: UserEntity) : HomeAction()
    }

    private val userAction = MutableStateFlow<HomeAction>(HomeAction.Loading)
    val userState = userAction.asStateFlow()

    fun fetchUsersById(userId: String) {
        viewModelScope.launch {
            getUserByIdUseCase(userId).collect { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        userAction.value = HomeAction.Loading
                    }
                    Resource.Status.SUCCESS -> {
                        if (resource.data != null) {
                            userAction.value = HomeAction.Successful(resource.data)
                        }
                    }
                    Resource.Status.ERROR -> {
                        userAction.value = HomeAction.Error
                    }
                }
            }
        }
    }
}