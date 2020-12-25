package com.bj.databasedemo.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import com.bj.databasedemo.db.NameTuple
import com.bj.databasedemo.db.User

@Dao
interface UserDao {

    @Update
    fun updateUsers(vararg users: User)

    @RawQuery
    fun getAllUsers(sql: SimpleSQLiteQuery): Array<User>

    @Query("select * from user")
    fun getAllUser(): Array<User>

    @Query("SELECT * FROM user WHERE userId = :uid")
    fun getAllUserById(uid: String): Array<User>

    @Query("SELECT * FROM user WHERE userId LIKE :uid OR name LIKE :name")
    fun getAllUserByIdOrName(uid: String, name: String): Array<User>

    @Query("SELECT userId,name FROM user")
    fun getName(): List<NameTuple>
}