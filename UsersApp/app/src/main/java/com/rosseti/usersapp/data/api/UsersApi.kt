package com.rosseti.usersapp.data.api

import com.rosseti.usersapp.data.model.UserModel
import com.rosseti.usersapp.data.model.UsersResponse
import retrofit2.http.*

interface UsersApi {
    @GET(value = "users")
    suspend fun fetchUsers(): UsersResponse

    @GET(value = "users/{userId}")
    suspend fun fetchUserById(@Path("userId") userId: String): UserModel

    @FormUrlEncoded
    @PUT(value = "users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: String,
        @Field("name") name: String,
        @Field("biography") biography: String
    ): UserModel

    @DELETE(value = "users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: String): Unit
}