package com.legend.techtask.viewModel.viewModelProviderFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.legend.techtask.repository.ConversationRepository
import com.legend.techtask.viewModel.ConversationViewModel

class ConversationViewModelProviderFactory(
    val app: Application,
    private val repository: ConversationRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConversationViewModel(app,repository) as T
    }
}
