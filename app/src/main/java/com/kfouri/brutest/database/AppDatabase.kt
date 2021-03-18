package com.kfouri.brutest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kfouri.brutest.database.dao.SubscriptionDao
import com.kfouri.brutest.database.model.Subscription

@Database(entities = [Subscription::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subscriptionDao(): SubscriptionDao

}