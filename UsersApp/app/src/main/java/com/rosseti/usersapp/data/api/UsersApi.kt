package com.rosseti.usersapp.data.api

import com.rosseti.usersapp.data.model.UsersResponse
import retrofit2.http.GET

interface UsersApi {
    @GET(value = "users")
    suspend fun fetchUsers(): UsersResponse
}