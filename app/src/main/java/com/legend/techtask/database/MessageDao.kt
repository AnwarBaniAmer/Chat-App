package com.legend.techtask.database

import androidx.paging.PagingSource
import androidx.room.*
import com.legend.techtask.model.Message
import com.legend.techtask.model.MessagesAndUsers

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAllMessages(messages: List<Message>)

    @Query("SELECT * FROM message ORDER BY id DESC")
     fun getAllMessages(): List<Message>

    @Query("DELETE FROM message WHERE id LIKE :messageId")
     fun deleteMessage(messageId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAMessage(message: Message?) :Long

    /** Get messages from room DB with pagination */
    @Transaction
    @Query("SELECT * FROM message ORDER BY id DESC")
    fun getPagedMessagesAndUsers(): PagingSource<Int, MessagesAndUsers>
}