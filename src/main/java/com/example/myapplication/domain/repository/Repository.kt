package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.MovieResponse
import com.example.myapplication.utils.ApiResponse

interface Repository {

    suspend fun getPopularMovies(): ApiResponse<MovieResponse>
    suspend fun getSearchedMovies(): ApiResponse<MovieResponse>

}