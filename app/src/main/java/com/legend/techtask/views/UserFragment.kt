package com.legend.techtask.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.legend.techtask.adapter.UsersAdapter
import com.legend.techtask.database.ConversationDatabase
import com.legend.techtask.databinding.FragmentUserBinding
import com.legend.techtask.repository.UsersRepository
import com.legend.techtask.utils.Utils
import com.legend.techtask.viewModel.viewModelProviderFactory.UserViewModelProviderFactory
import com.legend.techtask.viewModel.UsersViewModel

class UserFragment : Fragment() {
    private lateinit var userViewModel: UsersViewModel
    private lateinit var usersAdapter: UsersAdapter
    private var _binding: FragmentUserBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewModel()
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initViewModel() {
        val repository = UsersRepository(ConversationDatabase(requireContext()))
        val viewModelProviderFactory =
            UserViewModelProviderFactory(
                (activity as HomeActivity).application,
                repository
            )
        userViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[UsersViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        fetchAllUsersMessages()
    }

    private fun setUpView() {
        Utils.hideSoftKeyboard(activity as HomeActivity)
        usersAdapter = UsersAdapter()
        binding.rvUsers.apply {
            adapter = usersAdapter
        }
    }

    private fun fetchAllUsersMessages() {
        userViewModel.getUsersMessagesList().observe(viewLifecycleOwner, { usersMessagesList ->
            usersAdapter.differ.submitList(usersMessagesList.toList())
        })
    }
}