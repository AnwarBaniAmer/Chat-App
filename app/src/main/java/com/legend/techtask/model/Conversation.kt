package com.legend.techtask.model

import androidx.room.PrimaryKey


data class Conversation(
    @PrimaryKey
    val id:Int,
    val messages: MutableList<Message>,
    val users: List<User>
)