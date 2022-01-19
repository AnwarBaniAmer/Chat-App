package com.legend.techtask.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.legend.techtask.R
import com.legend.techtask.model.Conversation
import com.legend.techtask.model.Message
import com.legend.techtask.model.User
import com.legend.techtask.repository.ConversationRepository
import com.legend.techtask.utils.Utils
import com.legend.techtask.utils.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class ConversationViewModel(
    app: Application,
    private val repository: ConversationRepository
) : AndroidViewModel(app) {
    private val context by lazy { getApplication<Application>().applicationContext }

    private var _conversation: MutableLiveData<ViewState<Conversation>> = MutableLiveData()
    fun getConversation(): LiveData<ViewState<Conversation>> = _conversation

    private var conversationApiResponse: Conversation? = null

    fun loadConversation() {
        _conversation.postValue(ViewState.Loading)

        viewModelScope.launch {
            try {
                if (Utils.isConnected(getApplication())) {
                    val conversationResponse = repository.getRemoteConversation()
                    _conversation.postValue(handleConversationResponse(conversationResponse))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _conversation.postValue(
                        ViewState.Error(
                            context.resources.getString(
                                R.string.network_failure
                            )
                        )
                    )
                    else -> _conversation.postValue(
                        ViewState.Error(
                            context.resources.getString(
                                R.string.conversion_error
                            )
                        )
                    )
                }
            }
        }
    }

    private fun handleConversationResponse(response: Response<Conversation>): ViewState<Conversation> {
        try {
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    viewModelScope.launch { insertAllMessages(resultResponse.messages) }
                    viewModelScope.launch { insertAllUsers(resultResponse.users) }
                    return ViewState.Success(conversationApiResponse ?: resultResponse)
                }
            } else
                return ViewState.Error(response.errorBody().toString())

        } catch (t: Throwable) {
            Timber.e(t.message)
            return ViewState.Error(t.message.toString())
        }
        return ViewState.Error(response.message())
    }

    private suspend fun insertAllUsers(usersList: List<User>) {
        withContext(Dispatchers.IO) {
            repository.insertAllUsers(usersList)
        }
    }

    private suspend fun insertAllMessages(messagesList: List<Message>) {
        withContext(Dispatchers.IO) {
            repository.insertAllMessages(messagesList)
        }
    }
}