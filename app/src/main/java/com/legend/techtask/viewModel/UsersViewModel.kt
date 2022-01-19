package com.legend.techtask.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.legend.techtask.model.UsersAndMessages
import com.legend.techtask.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersViewModel(
    app: Application,
    private val repository: UsersRepository
) : AndroidViewModel(app) {

    private var _usersMessagesList: MutableLiveData<List<UsersAndMessages>> = MutableLiveData()
    fun getUsersMessagesList(): LiveData<List<UsersAndMessages>> = _usersMessagesList

    init {
        viewModelScope.launch {
            getUsersMessages()
        }
    }
    private suspend fun getUsersMessages() {
        withContext(Dispatchers.IO) {
            val usersAndMessages = repository.getUsersMessages()
            _usersMessagesList.postValue(usersAndMessages)
        }
    }
}