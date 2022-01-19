package com.legend.techtask.model

import androidx.room.*
import com.legend.techtask.database.Converters


@Entity(
    tableName = "message"
)
@TypeConverters(Converters::class)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val attachments: List<Attachment>?,
    val content: String,
    val userId: Int

) {
    constructor(content: String, userId: Int) : this(null, null, content, userId)

    fun isSender(): Boolean = userId == 1
}