package com.kfouri.brutest.model

import com.google.gson.annotations.SerializedName

data class GenresResponse (
    @SerializedName("genres") val genres: ArrayList<Genres>,
)

data class Genres (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
)