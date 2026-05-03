package com.example.myapplication.di

import com.example.myapplication.data.remote.MovieService
import com.example.myapplication.data.repository.RepositoryImpl
import com.example.myapplication.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/").addConverterFactory(
        GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): MovieService = retrofit.create<MovieService>(MovieService::class.java)

    @Provides
    @Singleton
    fun provideRepository(service: MovieService): Repository = RepositoryImpl(service)

}