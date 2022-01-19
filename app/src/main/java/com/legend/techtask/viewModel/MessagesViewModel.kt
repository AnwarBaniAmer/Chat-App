package com.legend.techtask.viewModel

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn

import com.legend.techtask.model.Message
import com.legend.techtask.model.MessagesAndUsers
import com.legend.techtask.repository.MessagesRepository
import com.legend.techtask.utils.Constants.Companion.QUERY_PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MessagesViewModel(
    app: Application,
    private val repository: MessagesRepository
) : AndroidViewModel(app) {

    val inputMessageContent = MutableLiveData<String>()

    val pagedMessagesAndUsers: Flow<PagingData<MessagesAndUsers>> = Pager(
        PagingConfig(pageSize = QUERY_PAGE_SIZE, enablePlaceholders = true, maxSize = 150)
    ) {

        repository.getPagedMessagesAndUsers()
    }.flow.cachedIn(viewModelScope)

    suspend fun insertNewMessage(message: Message) =
        withContext(Dispatchers.IO) {
            repository.insertAMessage(message)
        }

    suspend fun deleteMessage(messageId: Int) {
        withContext(Dispatchers.IO) {
            repository.deleteMessage(messageId)
        }
    }
}