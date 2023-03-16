package com.example.fitpeoassignment.di

import com.example.fitpeoassignment.data.ImagesRepositary
import com.example.fitpeoassignment.data.ImagesRespositaryImpl
import com.example.fitpeoassignment.data.NetworkService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideImagesApi():NetworkService{
        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                .setLenient()
                .create()))
            .baseUrl("https://jsonplaceholder.typicode.com")
            .build()
            .create(NetworkService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepositary(imagesApi:NetworkService):ImagesRepositary{
        return ImagesRespositaryImpl(imagesApi)

    }

}