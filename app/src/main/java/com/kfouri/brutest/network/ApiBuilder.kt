package com.kfouri.brutest.network

import com.kfouri.brutest.util.BASE_URL
import com.kfouri.brutest.util.TOKEN
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Request

object ApiBuilder {

    private var client = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $TOKEN")
            .build()
        chain.proceed(newRequest)
    }.build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}