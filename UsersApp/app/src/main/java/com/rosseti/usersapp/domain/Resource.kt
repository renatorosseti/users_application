package com.rosseti.usersapp.domain

data class Resource<out T>(val status: Status, val data: T?, val error: Throwable?) {
    companion object {
        fun <T> success(data: T? = null): Resource<T> =
            Resource(
                Status.SUCCESS,
                data,
                null
            )

        fun <T> error(error: Throwable? = null, data: T? = null): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                error
            )
        }

        fun <T> errorNetwork(error: Throwable? = null, data: T? = null): Resource<T> =
            Resource(
                Status.NETWORK_ERROR,
                data,
                error
            )

        fun <T> loading(data: T? = null): Resource<T> =
            Resource(
                Status.LOADING,
                data,
                null
            )
    }

    enum class Status {
        SUCCESS,
        ERROR,
        NETWORK_ERROR,
        LOADING
    }
}