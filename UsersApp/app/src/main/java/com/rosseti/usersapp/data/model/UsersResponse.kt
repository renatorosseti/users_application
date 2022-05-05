package com.rosseti.usersapp.data.model

import com.google.gson.annotations.SerializedName

class UsersResponse : ArrayList<UserModel>()

data class UserModel(
    val id: String = "",
    @SerializedName("avatar_image") val image: String = "",
    var name: String = "",
    var biography: String = ""
)