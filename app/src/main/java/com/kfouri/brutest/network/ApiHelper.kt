package com.kfouri.brutest.network

class ApiHelper(private val apiService: ApiService) {
    suspend fun getMovies(page: Long) = apiService.getMovies(page)
    suspend fun getGenres() = apiService.getGenres()
    suspend fun getMovieDetail(id: Long) = apiService.getMovieDetail(id)
}