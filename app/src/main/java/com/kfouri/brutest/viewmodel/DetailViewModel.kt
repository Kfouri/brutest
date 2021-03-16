package com.kfouri.brutest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kfouri.brutest.network.ApiRepository
import com.kfouri.brutest.util.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class DetailViewModel(private val apiRepository: ApiRepository): ViewModel() {
    fun getMovieDetail(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.getMovieDetail(id)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error getting movies..."))
        }
    }
}