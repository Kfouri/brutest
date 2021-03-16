package com.kfouri.brutest.network

class ApiHelper(private val apiService: ApiService) {
    suspend fun getMovies() = apiService.getMovies()
    suspend fun getGenres() = apiService.getGenres()
    suspend fun getMovieDetail(id: Int) = apiService.getMovieDetail(id)
}