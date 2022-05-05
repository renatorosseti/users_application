package com.rosseti.usersapp.domain.usecase

import com.rosseti.usersapp.domain.repository.UsersRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(private val repository: UsersRepository) {
    suspend operator fun invoke(userId: String): Unit {
        repository.deleteUser(userId)
    }
}