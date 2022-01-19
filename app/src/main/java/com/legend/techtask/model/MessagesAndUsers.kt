package com.legend.techtask.model

import androidx.room.Embedded
import androidx.room.Relation


data class MessagesAndUsers(
    @Embedded val message: Message,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: User
)

