package com.rosseti.usersapp.domain.repository

import com.rosseti.usersapp.domain.entity.UserEntity

interface UsersRepository {
    suspend fun fetchUsers(): List<UserEntity>
    suspend fun fetchUserById(userId: String): UserEntity
}