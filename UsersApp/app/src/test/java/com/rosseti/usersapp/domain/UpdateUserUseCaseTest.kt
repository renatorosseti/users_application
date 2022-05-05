package com.rosseti.usersapp.domain

import com.rosseti.usersapp.domain.entity.UserEntity
import com.rosseti.usersapp.domain.repository.UsersRepository
import com.rosseti.usersapp.domain.usecase.GetUserByIdUseCase
import com.rosseti.usersapp.domain.usecase.UpdateUserUseCase
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
class UpdateUserUseCaseTest {
    private lateinit var updateUserUseCase: UpdateUserUseCase

    @MockK(relaxed = true)
    private lateinit var repository: UsersRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateUserUseCase =
            UpdateUserUseCase(
                repository
            )
    }

    @Test
    fun `when request updateUser with valid credentials returns UserEntity, emit success with content as data`() {
        val userId = "Test"
        val name = "Test"
        val biography = "Test"
        val response = UserEntity(
                name = "Test 001",
                biography = "Test"
            )

        coEvery { repository.updateUser(userId = userId, name = name, biography = biography) } returns response
        runBlocking {
            updateUserUseCase(userId = userId, name = name, biography = biography).collectIndexed { index, value ->
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
    fun `when request updateUser returns error, emit IOException error`() {
        val userId = "Test"
        val name = "Test"
        val biography = "Test"
        coEvery { repository.updateUser(userId = userId, name = name, biography = biography) } throws IOException("IO error")

        runBlocking {
            updateUserUseCase(userId = userId, name = name, biography = biography).collectIndexed { index, value ->
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