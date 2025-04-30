package com.example.gripnotes.di

import com.example.gripnotes.model.AuthServiceI
import com.example.gripnotes.model.FirebaseAuthService
import com.example.gripnotes.model.FirestoreRepository
import com.example.gripnotes.model.RepositoryI
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        repository: FirestoreRepository
    ): RepositoryI

    @Binds
    @Singleton
    abstract fun bindAuthService(
        authService: FirebaseAuthService
    ): AuthServiceI
}