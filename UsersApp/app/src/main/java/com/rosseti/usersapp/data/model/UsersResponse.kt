package com.rosseti.usersapp.data.model

import com.google.gson.annotations.SerializedName

class UsersResponse : ArrayList<User>()

data class User(
    val id: String,
    @SerializedName("avatar_image") val image: String,
    val name: String,
    val biography: String
)