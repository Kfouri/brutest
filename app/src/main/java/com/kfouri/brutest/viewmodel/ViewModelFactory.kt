package com.kfouri.brutest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kfouri.brutest.database.DatabaseHelper
import com.kfouri.brutest.network.ApiHelper
import com.kfouri.brutest.network.ApiRepository

class ViewModelFactory(private val apiHelper: ApiHelper, private val databaseHelper: DatabaseHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(ApiRepository(apiHelper), databaseHelper) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(ApiRepository(apiHelper), databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}