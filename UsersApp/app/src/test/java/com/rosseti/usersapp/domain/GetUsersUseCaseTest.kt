package com.rosseti.usersapp.domain

import com.rosseti.usersapp.domain.entity.UserEntity
import com.rosseti.usersapp.domain.repository.UsersRepository
import com.rosseti.usersapp.domain.usecase.GetUsersUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class GetUsersUseCaseTest {
    private lateinit var getUsersUseCase: GetUsersUseCase

    @MockK(relaxed = true)
    private lateinit var repository: UsersRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getUsersUseCase =
            GetUsersUseCase(
                repository,
            )
    }

    @Test
    fun `when request fetchUsers with valid credentials returns UserEntity list, emit success with content as data`() {
        val response = listOf(
            UserEntity(
                name = "Test 001",
                biography = "Test"
            )
        )
        coEvery { repository.fetchUsers() } returns response
        runBlocking {
            getUsersUseCase().collectIndexed { index, value ->
                when (index) {
                    FIRST_ELEMENT_EMITTED -> {
                        assert(value.status == Resource.Status.LOADING)
                    }
                    SECOND_ELEMENT_EMITTED -> {
                        assert(value.status == Resource.Status.SUCCESS)
                        assert(value.data == response)
                    }
                }
            }
        }
    }

    @Test
    fun `when request fetchUsers returns error, emit IOException error`() {
        coEvery { repository.fetchUsers() } throws IOException("IO error")

        runBlocking {
            getUsersUseCase().collectIndexed { index, value ->
                when (index) {
                    FIRST_ELEMENT_EMITTED -> {
                        assert(value.status == Resource.Status.LOADING)
                    }
                    SECOND_ELEMENT_EMITTED -> {
                        assert(value.status == Resource.Status.ERROR)
                        assert(value.data == null)
                        assert(value.error is IOException)
                    }
                }
            }
        }
    }

    companion object {
        const val FIRST_ELEMENT_EMITTED = 0
        const val SECOND_ELEMENT_EMITTED = 1
    }
}