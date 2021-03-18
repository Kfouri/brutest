package com.kfouri.brutest.database

import com.kfouri.brutest.database.model.Subscription

interface DatabaseHelper {

    suspend fun getSubscription(id: Long): Subscription

    suspend fun getAllSubscriptions(): List<Subscription>

    suspend fun insertSubscription(subscription: Subscription)

    suspend fun updateSubscription(subscription: Subscription)
}