package com.kfouri.brutest.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kfouri.brutest.database.model.Subscription

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM subscription where id = :id")
    suspend fun getSubscription(id: Long): Subscription

    @Query("SELECT * FROM subscription WHERE subscribed = 1")
    suspend fun getAllSubscriptions(): List<Subscription>

    @Insert
    suspend fun insertSubscription(subscription: Subscription)

    @Update
    suspend fun updateSubscription(subscription: Subscription)
}