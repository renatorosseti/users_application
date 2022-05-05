package com.rosseti.usersapp.di

import com.rosseti.usersapp.data.repository.UsersRepositoryImpl
import com.rosseti.usersapp.domain.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun provideUsersRepository(repository: UsersRepositoryImpl) : UsersRepository
}