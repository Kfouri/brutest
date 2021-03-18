package com.kfouri.brutest.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Subscription(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "poster") val poster: String,
    @ColumnInfo(name = "subscribed") val subscribed: Boolean
)