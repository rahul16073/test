package com.example.myapplication.utils

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T): ApiResponse<T>()
    data class Error<T>(val data: T?=null, val message: String): ApiResponse<T>()
}

