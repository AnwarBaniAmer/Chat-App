package com.legend.techtask.database

import androidx.room.*
import com.legend.techtask.model.User
import com.legend.techtask.model.UsersAndMessages

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(usersList: List<User>)

    @Transaction
    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getUsersAndMessages(): List<UsersAndMessages>
}