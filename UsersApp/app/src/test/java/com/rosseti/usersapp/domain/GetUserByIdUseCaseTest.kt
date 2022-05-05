package com.rosseti.usersapp.domain

import com.rosseti.usersapp.domain.entity.UserEntity
import com.rosseti.usersapp.domain.repository.UsersRepository
import com.rosseti.usersapp.domain.usecase.GetUserByIdUseCase
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
class GetUserByIdUseCaseTest {
    private lateinit var getUserByIdUseCase: GetUserByIdUseCase

    @MockK(relaxed = true)
    private lateinit var repository: UsersRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getUserByIdUseCase =
            GetUserByIdUseCase(
                repository
            )
    }

    @Test
    fun `when request fetchUserById with valid credentials returns UserEntity, emit success with content as data`() {
        val userId = "Test"
        val response = UserEntity(
                name = "Test 001",
                biography = "Test"
            )

        coEvery { repository.fetchUserById(userId = userId) } returns response
        runBlocking {
            getUserByIdUseCase(userId).collectIndexed { index, value ->
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
    fun `when request fetchUserById returns error, emit IOException error`() {
        val userId = "Test"
        coEvery { repository.fetchUserById(userId = userId) } throws IOException("IO error")

        runBlocking {
            getUserByIdUseCase(userId).collectIndexed { index, value ->
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