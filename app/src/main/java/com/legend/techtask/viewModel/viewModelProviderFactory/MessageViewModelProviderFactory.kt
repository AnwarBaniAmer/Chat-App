package com.legend.techtask.viewModel.viewModelProviderFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.legend.techtask.repository.MessagesRepository
import com.legend.techtask.viewModel.MessagesViewModel

class MessageViewModelProviderFactory(
    val app: Application,
    private val repository: MessagesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MessagesViewModel(app,repository) as T
    }
}
