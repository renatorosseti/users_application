package com.rosseti.usersapp.data.api

import com.rosseti.usersapp.data.model.UserModel
import com.rosseti.usersapp.data.model.UsersResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersApi {
    @GET(value = "users")
    suspend fun fetchUsers(): UsersResponse

    @GET(value = "users/{userId}")
    suspend fun fetchUserById(@Path("userId") userId: String): UserModel

    @PUT(value = "users/{userId}")
    suspend fun updateUser(@Path("userId") userId: String): UserModel

    @DELETE(value = "users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: String): Unit
}