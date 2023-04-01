package com.example.searchuser.di

import com.example.searchuser.utils.Constants.BASE_URL
import com.example.searchuser.data.SearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val interceptor = OkHttpClient.Builder().addInterceptor(logger).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(interceptor)
        return retrofit.build()
    }

    @Provides
    @Singleton
    fun providesSearchApiInterface(): SearchApi{
        return providesRetrofit().create(SearchApi::class.java)
    }
}