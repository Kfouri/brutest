package com.kfouri.brutest.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.kfouri.brutest.database.DatabaseHelper
import com.kfouri.brutest.database.model.Subscription
import com.kfouri.brutest.model.Movie
import com.kfouri.brutest.network.ApiRepository
import com.kfouri.brutest.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import com.kfouri.brutest.util.IMAGES_URL

class SearchViewModel(private val apiRepository: ApiRepository, private val databaseHelper: DatabaseHelper): ViewModel() {

    private val TAG = "SearchViewModel"

    fun searchMovie(query: String, page: Long) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = apiRepository.searchMovie(query, page)
            val searchList = response.results

            searchList.forEach { movie ->
                databaseHelper.getSubscription(movie.id)?.let {subscription ->
                    if (subscription.subscribed) {
                        movie.subscribed = true
                    }
                }
            }

            response.results = searchList

            emit(Resource.success(data = response))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error getting movies..."))
        }
    }

    fun updateSubscription(movie: Movie) {
        viewModelScope.launch {
            val sub = Subscription(movie.id, IMAGES_URL + movie.posterPath, !movie.subscribed)
            try {
                databaseHelper.insertSubscription(sub)
            } catch (e: Exception) {
                //Already exists in database
                try {
                    databaseHelper.updateSubscription(sub)
                } catch (e: Exception) {
                    Log.d(TAG, "Error: "+e.message)
                }
            }
        }
    }
}