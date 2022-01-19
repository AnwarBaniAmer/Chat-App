package com.legend.techtask.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.legend.techtask.R

import com.legend.techtask.database.ConversationDatabase
import com.legend.techtask.databinding.ActivityHomeBinding
import com.legend.techtask.repository.ConversationRepository
import com.legend.techtask.utils.ViewState
import com.legend.techtask.viewModel.ConversationViewModel
import com.legend.techtask.viewModel.viewModelProviderFactory.ConversationViewModelProviderFactory
import com.legend.techtask.utils.Constants.Companion.API_LUNCHED
import com.legend.techtask.utils.Utils

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: ConversationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initNavigationController()
        internetConnectionStatus()
        fetchConversationData()
    }

    private fun initViewModel() {
        val repository = ConversationRepository(ConversationDatabase(this))
        val viewModelProviderFactory =
            ConversationViewModelProviderFactory(
                (this).application,
                repository
            )
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[ConversationViewModel::class.java]
    }

    private fun initNavigationController() {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_messages,
                R.id.navigation_users
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun internetConnectionStatus() {
        val apiLunched = getPreferences(MODE_PRIVATE).getBoolean(
            API_LUNCHED, false
        )
        if (!Utils.isConnected(this) && !apiLunched) {
            Glide.with(this).load(R.drawable.no_internet_connection)
                .into(binding.noInternetConnection)
            binding.noInternetConnection.visibility = View.VISIBLE
        }
    }

    private fun fetchConversationData() {
        val apiLunched = getPreferences(MODE_PRIVATE).getBoolean(API_LUNCHED, false)
        if (!apiLunched) {
            viewModel.loadConversation()
            viewModel.getConversation().observe(this, {
                when (it) {
                    is ViewState.Loading -> {
                        showProgressBar()
                    }
                    is ViewState.Success -> {
                        hideProgressBar()
                        val prefs = getPreferences(MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.putBoolean(
                            API_LUNCHED, true
                        )
                        editor.apply()
                    }
                    is ViewState.Error -> {
                        hideProgressBar()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }

                    else -> ViewState.Idle
                }
            })
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
}