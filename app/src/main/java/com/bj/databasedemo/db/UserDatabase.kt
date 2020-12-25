package com.bj.databasedemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bj.databasedemo.dao.UserDao


@Database(entities = [User::class, Book::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao?

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        fun getInstance(context: Context): UserDatabase? {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder<UserDatabase>(
                            context.applicationContext,
                            UserDatabase::class.java, "BookSystem.db"
                        ).build()
                    }
                }
            }
            return INSTANCE
        }
    }
}