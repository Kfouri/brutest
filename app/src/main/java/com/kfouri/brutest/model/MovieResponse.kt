package com.kfouri.brutest.model

import com.google.gson.annotations.SerializedName

data class MovieResponse (
        @SerializedName("page") val page: Long,
        @SerializedName("results") var results: ArrayList<Movie>,
        @SerializedName("total_pages") val totalPages: Long,
)

data class Movie (
    @SerializedName("id") val id: Long,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("genre_ids") val genreIds: ArrayList<Int>,
    @Transient var subscribed: Boolean
)