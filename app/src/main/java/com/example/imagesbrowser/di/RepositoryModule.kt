package com.example.imagesbrowser.di

import com.example.imagesbrowser.repository.RepositoryImpl
import com.example.imagesbrowser.webservice.PicsumService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepositoryImpl(
        webService: PicsumService
    ): RepositoryImpl = RepositoryImpl(webService)
}