package com.rosseti.usersapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosseti.usersapp.domain.Resource
import com.rosseti.usersapp.domain.entity.UserEntity
import com.rosseti.usersapp.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getUsersUseCase: GetUsersUseCase) :
    ViewModel() {

    sealed class HomeAction {
        object Loading : HomeAction()
        object Error : HomeAction()
        data class Successful(val data: List<UserEntity>) : HomeAction()
    }

    private val homeAction = MutableStateFlow<HomeAction>(HomeAction.Loading)
    val homeState = homeAction.asStateFlow()

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            getUsersUseCase().collect { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        homeAction.value = HomeAction.Loading
                    }
                    Resource.Status.SUCCESS -> {
                        if (resource.data != null) {
                            homeAction.value = HomeAction.Successful(resource.data)
                        }
                    }
                    Resource.Status.ERROR -> {
                        homeAction.value = HomeAction.Error
                    }
                }
            }
        }
    }
}