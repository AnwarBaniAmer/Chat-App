package com.legend.techtask.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "user"
)
data class User(
    @PrimaryKey
    val id: Int,
    val avatarId: String,
    val name: String
)