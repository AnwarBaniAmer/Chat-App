package com.legend.techtask.model

import androidx.room.Embedded
import androidx.room.Relation

data class UsersAndMessages(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val messagesList: List<Message>
)