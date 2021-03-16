package com.kfouri.brutest.network

import com.kfouri.brutest.model.DetailResponse
import com.kfouri.brutest.model.GenresResponse
import com.kfouri.brutest.model.MovieResponse
import com.kfouri.brutest.util.APIKEY
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("4/discover/movie?sort_by=popularity.desc&language=es-ES")
    suspend fun getMovies(): MovieResponse

    @GET("3/genre/movie/list?api_key=$APIKEY&language=es-ES")
    suspend fun getGenres(): GenresResponse

    @GET("3/movie/{id}?api_key=$APIKEY&language=es-ES")
    suspend fun getMovieDetail(@Path("id") id: Int): DetailResponse
}