package com.legend.techtask.viewModel.viewModelProviderFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.legend.techtask.repository.UsersRepository
import com.legend.techtask.viewModel.UsersViewModel

class UserViewModelProviderFactory(
    val app: Application,
    private val repository: UsersRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsersViewModel(app,repository) as T
    }
}
