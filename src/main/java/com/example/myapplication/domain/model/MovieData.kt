package com.example.myapplication.domain.model

data class MovieData(val title : String, val poster_path: String, val vote_average: Double)
data class MovieResponse(val results: List<MovieData>)