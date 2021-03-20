package com.kfouri.brutest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kfouri.brutest.database.DatabaseHelper
import com.kfouri.brutest.network.ApiHelper
import com.kfouri.brutest.network.ApiRepository

class ViewModelFactory(private val apiHelper: ApiHelper, private val databaseHelper: DatabaseHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListViewModel::class.java) -> {
                ListViewModel(ApiRepository(apiHelper), databaseHelper) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(ApiRepository(apiHelper), databaseHelper) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(ApiRepository(apiHelper), databaseHelper) as T
            }
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }

}