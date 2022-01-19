package com.legend.techtask.repository

import androidx.paging.PagingSource
import com.legend.techtask.database.ConversationDatabase
import com.legend.techtask.model.Message
import com.legend.techtask.model.MessagesAndUsers

class MessagesRepository(
    private val db: ConversationDatabase,
) {
    fun insertAMessage(message: Message) = db.getMessageDao().insertAMessage(message)

    fun deleteMessage(messageId: Int) = db.getMessageDao().deleteMessage(messageId)
    fun getPagedMessagesAndUsers(): PagingSource<Int, MessagesAndUsers> {
        return db.getMessageDao().getPagedMessagesAndUsers()
    }
}

