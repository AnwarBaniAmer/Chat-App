package com.legend.techtask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.legend.techtask.model.Message
import com.legend.techtask.model.User
import com.legend.techtask.utils.Constants.Companion.DATABASE_NAME

@Database(entities = [User::class, Message::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ConversationDatabase : RoomDatabase() {

    abstract fun getMessageDao(): MessageDao
    abstract fun getUserDao(): UserDao

    companion object {

        @Volatile
        private var instance: ConversationDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ConversationDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }
}