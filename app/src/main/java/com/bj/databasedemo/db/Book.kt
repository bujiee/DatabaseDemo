package com.bj.databasedemo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserBook")
data class Book(
    /**
     *  bookId
     */
    @PrimaryKey
    @ColumnInfo(name = "bookId")
    val bookId: Int,

    /**
     * num
     */
    val num: String?,

    /**
     * price
     */
    @ColumnInfo(name = "bookPrice")
    val price: String
)