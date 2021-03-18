package com.kfouri.brutest.viewmodel

import androidx.lifecycle.*
import com.kfouri.brutest.database.DatabaseHelper
import com.kfouri.brutest.database.model.Subscription
import com.kfouri.brutest.network.ApiRepository
import com.kfouri.brutest.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel(private val apiRepository: ApiRepository, private val databaseHelper: DatabaseHelper): ViewModel() {

    private val subscribedLiveData = MutableLiveData<Boolean>()

    fun getMovieDetail(id: Long) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.getMovieDetail(id)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error getting movies..."))
        }
    }

    fun isSubscribed(id: Long) {
        viewModelScope.launch {
            var subscribed = false
            try {
                subscribed = databaseHelper.getSubscription(id).subscribed
            } catch (e: Exception) {
                //Does not exists in database, that means this movie is not subscribed.
            }
            subscribedLiveData.postValue(subscribed)
        }
    }

    fun updateSubscription(id: Long, state: Boolean, poster: String) {
        viewModelScope.launch {
            val sub = Subscription(id, poster, state)
            try {
                databaseHelper.insertSubscription(sub)
            } catch (e: Exception) {
                //Already exists in database
                databaseHelper.updateSubscription(sub)
            }
            subscribedLiveData.postValue(state)
        }
    }

    fun onSubscribed(): LiveData<Boolean> = subscribedLiveData

}