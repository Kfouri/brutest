package com.kfouri.brutest.network

class ApiRepository(private val apiHelper: ApiHelper) {
    suspend fun getMovies(page: Long) = apiHelper.getMovies(page)
    suspend fun getGenres() = apiHelper.getGenres()
    suspend fun getMovieDetail(id: Long) = apiHelper.getMovieDetail(id)
}