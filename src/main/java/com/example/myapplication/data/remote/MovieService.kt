package com.example.myapplication.data.remote

import com.example.myapplication.domain.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface  MovieService {
    @GET("/3/movie/popular")
    @Headers("accept: application/json" )
    suspend fun getPopularMovies(
        @Header("Authorization") authorization: String,
        @Query("language") language: String,
        @Query("page") Page: Int
    ): Response<MovieResponse>

    @GET("/3/search/movie")
    @Headers("accept: application/json" )
    suspend fun getSearchedMovies(
        @Query("include_adult") include_adult: Boolean,
        @Query("language") language: String,
        @Query("page") Page: Int
    ): Response<MovieResponse>
}