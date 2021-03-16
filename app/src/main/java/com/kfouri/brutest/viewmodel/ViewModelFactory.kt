package com.kfouri.brutest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kfouri.brutest.network.ApiHelper
import com.kfouri.brutest.network.ApiRepository

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(ApiRepository(apiHelper)) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(ApiRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}