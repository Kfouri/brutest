package com.kfouri.brutest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kfouri.brutest.database.DatabaseHelper
import com.kfouri.brutest.network.ApiRepository
import com.kfouri.brutest.util.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class SearchViewModel(private val apiRepository: ApiRepository, private val databaseHelper: DatabaseHelper): ViewModel() {

    fun searchMovie(query: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val searchList = apiRepository.searchMovie(query).results

            searchList.forEach { movie ->
                databaseHelper.getSubscription(movie.id)?.let {subscription ->
                    if (subscription.subscribed) {
                        movie.subscribed = true
                    }
                }
            }

            emit(Resource.success(data = searchList))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error getting movies..."))
        }
    }
}