package com.legend.techtask.repository

import com.legend.techtask.api.RetrofitClientInstance
import com.legend.techtask.database.ConversationDatabase
import com.legend.techtask.model.User
import com.legend.techtask.model.Message

class ConversationRepository(
    private val db: ConversationDatabase,
) {
    suspend fun getRemoteConversation() = RetrofitClientInstance.api.getConversation()

    fun insertAllMessages(messagesList: List<Message>) =
        db.getMessageDao().insertAllMessages(messagesList)

    fun insertAllUsers(usersList: List<User>) =
        db.getUserDao().insertAllUsers(usersList)
}