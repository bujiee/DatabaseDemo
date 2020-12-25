package com.bj.databasedemo.db

import androidx.room.ColumnInfo

data class NameTuple(
    @ColumnInfo(name = "userId")
    val userId: Int?,
    @ColumnInfo(name = "name")
    val userName: String?
)

