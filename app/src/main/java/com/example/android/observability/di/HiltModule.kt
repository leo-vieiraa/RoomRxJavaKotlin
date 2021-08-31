package com.example.android.observability.di

import com.example.android.observability.repository.GithubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    fun provideGithubRepository() : GithubRepository = GithubRepository()

}