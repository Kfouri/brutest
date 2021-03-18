package com.kfouri.brutest.database

import com.kfouri.brutest.database.model.Subscription

class DatabaseHelperImpl(private val appDatabase: AppDatabase): DatabaseHelper {

    override suspend fun getSubscription(id: Long): Subscription = appDatabase.subscriptionDao().getSubscription(id)

    override suspend fun getAllSubscriptions(): List<Subscription> = appDatabase.subscriptionDao().getAllSubscriptions()

    override suspend fun insertSubscription(subscription: Subscription) = appDatabase.subscriptionDao().insertSubscription(subscription)

    override suspend fun updateSubscription(subscription: Subscription) = appDatabase.subscriptionDao().updateSubscription(subscription)

}