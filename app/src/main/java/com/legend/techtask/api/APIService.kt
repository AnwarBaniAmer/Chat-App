package com.legend.techtask.api

import com.legend.techtask.model.Conversation
import com.legend.techtask.utils.Constants.Companion.END_POINT_CONVERSATION
import retrofit2.Response
import retrofit2.http.GET

interface APIService {

    @GET(END_POINT_CONVERSATION)
    suspend fun getConversation(): Response<Conversation>
}