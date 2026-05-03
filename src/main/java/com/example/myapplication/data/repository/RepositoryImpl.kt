package com.example.myapplication.data.repository

import com.example.myapplication.data.remote.MovieService
import com.example.myapplication.domain.model.MovieResponse
import com.example.myapplication.domain.repository.Repository
import com.example.myapplication.utils.ApiResponse
import com.example.myapplication.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val service: MovieService): Repository {
    override suspend fun getPopularMovies(): ApiResponse<MovieResponse> {
        return withContext(Dispatchers.IO){
            try {
                val response = service.getPopularMovies(Utils.Authorization, "en-US", 1)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        return@withContext ApiResponse.Success(response.body() ) as ApiResponse<out MovieResponse>
                    } else {
                        return@withContext ApiResponse.Success<MovieResponse>(MovieResponse(emptyList()))
                    }
                } else {
                    return@withContext ApiResponse.Success(MovieResponse(emptyList()))
                }
            }catch (e: Exception){
                return@withContext ApiResponse.Error(null, "")
            }

        }
    }

    override suspend fun getSearchedMovies(): ApiResponse<MovieResponse> {
        return withContext(Dispatchers.IO){
            try {
                val response = service.getSearchedMovies( false, "en-US", 1)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        return@withContext ApiResponse.Success<MovieResponse>(response.body() as MovieResponse)
                    } else {
                        return@withContext ApiResponse.Success<MovieResponse>(MovieResponse(emptyList()))
                    }
                } else {
                    return@withContext ApiResponse.Success<MovieResponse>(MovieResponse(emptyList()))
                }
            }catch (e: Exception){
                return@withContext ApiResponse.Success<MovieResponse>(MovieResponse(emptyList()))
            }

        }
    }

}