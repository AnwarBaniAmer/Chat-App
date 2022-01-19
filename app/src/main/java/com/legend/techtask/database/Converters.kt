package com.legend.techtask.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.legend.techtask.model.Attachment

/**
 *
 * to converts the custom object type into a known type in terms of database types.
 */
class Converters {
    @TypeConverter
    fun fromAttachmentListToJson(attachments: List<Attachment>?): String? {
        return Gson().toJson(attachments)
    }

    @TypeConverter
    fun fromJsonToAttachmentList(json: String?): List<Attachment>? {
        if (json != "null") {
            return Gson().fromJson(json,Array<Attachment>::class.java).toList()
        }
        return  null
    }
}