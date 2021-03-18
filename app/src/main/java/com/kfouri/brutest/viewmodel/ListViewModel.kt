package com.kfouri.brutest.viewmodel

import androidx.lifecycle.*
import com.kfouri.brutest.database.DatabaseHelper
import com.kfouri.brutest.database.model.Subscription
import com.kfouri.brutest.network.ApiRepository
import com.kfouri.brutest.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ListViewModel(private val apiRepository: ApiRepository, private val databaseHelper: DatabaseHelper): ViewModel() {

    private val subscriptionsListLiveData = MutableLiveData<ArrayList<Subscription>>()

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

    fun getAllSubscriptions() {
        viewModelScope.launch {
            var subscriptionsList = ArrayList<Subscription>()
            try {
                subscriptionsList = databaseHelper.getAllSubscriptions() as ArrayList<Subscription>
            } catch (e: Exception) {
                //Database empty, that means no movie is subscribed.
            }
            subscriptionsListLiveData.postValue(subscriptionsList)
        }
    }

    fun onSubscriptionsList(): LiveData<ArrayList<Subscription>> = subscriptionsListLiveData

}