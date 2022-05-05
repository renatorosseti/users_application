package com.rosseti.usersapp.data.repository

import com.rosseti.usersapp.data.api.UsersApi
import com.rosseti.usersapp.domain.entity.UserEntity
import com.rosseti.usersapp.domain.repository.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(private val api: UsersApi) : UsersRepository {

    override suspend fun fetchUsers(): List<UserEntity> =
        api.fetchUsers().map {
            UserEntity(
                id = it.id,
                name = it.name,
                image = it.image,
                biography = it.biography
            )
        }

    override suspend fun fetchUserById(userId: String): UserEntity {
        val userModel = api.fetchUserById(userId = userId)
        return UserEntity(id = userModel.id, image = userModel.image, name = userModel.name, biography = userModel.biography)
    }
}