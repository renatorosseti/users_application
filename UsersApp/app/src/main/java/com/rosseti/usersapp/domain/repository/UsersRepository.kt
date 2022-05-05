package com.rosseti.usersapp.domain.repository

import com.rosseti.usersapp.domain.entity.UserEntity

interface UsersRepository {
    suspend fun fetchUsers(): List<UserEntity>
    suspend fun fetchUserById(userId: String): UserEntity
    suspend fun updateUser(userId: String, name: String, biography: String): UserEntity
    suspend fun deleteUser(userId: String): Unit
    suspend fun createUser(name: String, biography: String): UserEntity
}