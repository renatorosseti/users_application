package com.rosseti.usersapp.domain.usecase

import com.rosseti.usersapp.domain.Resource
import com.rosseti.usersapp.domain.entity.UserEntity
import com.rosseti.usersapp.domain.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val repository: UsersRepository) {
    operator fun invoke(name: String, biography: String): Flow<Resource<UserEntity>> =
        flow { emit(repository.createUser(name = name, biography = biography)) }
            .map { Resource.success(it) }
            .onStart { emit(Resource.loading()) }
            .catch { emit(Resource.error(it)) }
            .flowOn(Dispatchers.IO)
}