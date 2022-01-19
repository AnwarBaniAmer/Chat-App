package com.legend.techtask.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.legend.techtask.model.Message
import com.legend.techtask.model.User
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ConversationDatabaseTest : TestCase() {
    private lateinit var db: ConversationDatabase
    private lateinit var messageDao: MessageDao
    private lateinit var userDao: UserDao
    private lateinit var context: Context

    @Before
    public override fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(
            context, ConversationDatabase::class.java
        ).build()
        messageDao = db.getMessageDao()
        userDao = db.getUserDao()
    }

    @Test
    fun writeAndReadUserDao() = runBlocking {

        val user1 = User(11, "https://avatars.dicebear.com/api/avataaars/leanne.png", "Anwar")
        val user2 = User(12, "https://avatars.dicebear.com/api/avataaars/leanne.png", "Alaa")
        val user3 = User(13, "https://avatars.dicebear.com/api/avataaars/leanne.png", "Adam")
        val usersList = listOf(user1, user2, user3)

        userDao.insertAllUsers(usersList)

        val usersAndMessages = userDao.getUsersAndMessages()
        assertThat(usersAndMessages[0].user.name == "Anwar").isTrue()
        assertThat(usersAndMessages[0].user.id == 11).isTrue()

        assertThat(usersAndMessages[1].user.name == "Alaa").isTrue()
        assertThat(usersAndMessages[1].user.id == 13).isFalse()

        assertThat(usersAndMessages[2].user.name == "Baby Adam").isFalse()
        assertThat(usersAndMessages[2].user.id == 13).isTrue()
    }

    @Test
    fun writeAndReadMessageDao() = runBlocking {

        val msg1 = Message(1, null, "Hello I'm Anwar", 11)
        val msg2 = Message(2, null, "Hello I'm Alaa", 12)
        val msg3 = Message(3, null, "Hello I'm Adam", 13)
        val messageList = listOf(msg1, msg2, msg3)
        messageDao.insertAllMessages(messageList)
        val messages = messageDao.getAllMessages()

        assertThat(messages[0].userId == 11).isFalse()
        assertThat(messages[1].content == "Hello I'm Alaa").isTrue()
        assertThat(messages[2].userId == 11).isTrue()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}