package com.example.searchuser.di

import com.example.searchuser.data.SearchApi
import com.example.searchuser.domain.RepositoryImpl
import com.example.searchuser.domain.RepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesGetAllImagesRepositoryImpl(searchApi: SearchApi): RepositoryInterface {
        return RepositoryImpl(searchApi)
    }

}