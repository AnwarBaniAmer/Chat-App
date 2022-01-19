package com.legend.techtask.repository

import com.legend.techtask.database.ConversationDatabase

class UsersRepository(
    private val db: ConversationDatabase,
) {
    fun getUsersMessages() =
        db.getUserDao().getUsersAndMessages()
}