package com.bj.databasedemo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    /**
     * userId
     */
    @PrimaryKey
    val userId: Int?,

    /**
     * userName
     */
    @ColumnInfo(name = "name")
    val userName: String?
)