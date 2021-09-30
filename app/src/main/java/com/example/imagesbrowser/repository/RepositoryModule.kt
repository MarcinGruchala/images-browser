package com.example.imagesbrowser.repository

import com.example.imagesbrowser.networking.service.PicsumService
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
