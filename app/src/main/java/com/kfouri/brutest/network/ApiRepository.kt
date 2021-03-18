package com.kfouri.brutest.network

class ApiRepository(private val apiHelper: ApiHelper) {
    suspend fun getMovies() = apiHelper.getMovies()
    suspend fun getGenres() = apiHelper.getGenres()
    suspend fun getMovieDetail(id: Long) = apiHelper.getMovieDetail(id)
}