package com.kfouri.brutest.model

import com.google.gson.annotations.SerializedName

data class DetailResponse(
    @SerializedName("title") val title: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("backdrop_path") val backdropPath: String,
)