package com.kfouri.brutest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kfouri.brutest.network.ApiRepository
import com.kfouri.brutest.util.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class ListViewModel(private val apiRepository: ApiRepository): ViewModel() {

    fun getMovies() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.getMovies()))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error getting movies..."))
        }
    }

    fun getGenres() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.getGenres()))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error getting genres..."))
        }
    }
}