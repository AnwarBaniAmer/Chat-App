package com.legend.techtask.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.legend.techtask.R
import com.legend.techtask.adapter.MessageAdapter
import com.legend.techtask.database.ConversationDatabase
import com.legend.techtask.databinding.FragmentMessagesBinding
import com.legend.techtask.repository.MessagesRepository
import com.legend.techtask.viewModel.viewModelProviderFactory.MessageViewModelProviderFactory
import com.legend.techtask.viewModel.MessagesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.graphics.*
import android.os.Handler
import android.os.Looper

import androidx.lifecycle.*
import com.legend.techtask.model.Message
import com.legend.techtask.utils.Constants.Companion.SENDER_ID

import com.legend.techtask.utils.Utils
import com.legend.techtask.utils.Utils.RecyclerItemTouchHelper
import kotlinx.coroutines.runBlocking

class MessagesFragment : Fragment(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private lateinit var messagesViewModel: MessagesViewModel
    private lateinit var messageAdapter: MessageAdapter
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_messages, container, false
        )

        initViewModel()
        binding.messagesViewModel = messagesViewModel
        return binding.root
    }

    private fun initViewModel() {
        val messageRepository = MessagesRepository(ConversationDatabase(requireContext()))
        val viewModelProviderFactory =
            MessageViewModelProviderFactory(
                (activity as HomeActivity).application,
                messageRepository
            )

        messagesViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[MessagesViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        getPagedMessages()
    }

    private fun getPagedMessages() {
        showProgressBar()
        lifecycleScope.launch {
            messagesViewModel.pagedMessagesAndUsers.collectLatest { pagingData ->
                messageAdapter.submitData(pagingData)
            }
        }
        hideProgressBar()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpViews() {
        messageAdapter = MessageAdapter(requireContext())
        binding.rvConversation.apply {
            adapter = messageAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.fab.visibility = if (dy >= 0) View.GONE else View.VISIBLE
                }
            })
            setOnTouchListener { _, _ ->
                Utils.hideSoftKeyboard(activity as HomeActivity)
                false
            }

            val itemTouchHelperCallback: SimpleCallback =
                RecyclerItemTouchHelper(0, LEFT, this@MessagesFragment)
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
        }

        binding.fab.setOnClickListener {
            it.animate()
            binding.rvConversation.scrollToPosition(0)
        }
        binding.typeMsgContainer.btnSendMsg.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val messageContent = binding.typeMsgContainer.edtTypeMsg.text.toString()
        if (messageContent.isNotEmpty()) {
            binding.typeMsgContainer.btnSendMsg.playAnimation()
            val newMessage = Message(messageContent, SENDER_ID)

            runBlocking {
                messagesViewModel.insertNewMessage(newMessage)
            }

            Handler(Looper.getMainLooper()).postDelayed({
                binding.rvConversation.smoothScrollToPosition(0)
                binding.typeMsgContainer.edtTypeMsg.text?.clear()
            }, 300)
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is RecyclerView.ViewHolder) {
            val position = viewHolder.bindingAdapterPosition
            val name = messageAdapter.getMessage(position)?.user?.name
            val deletedItem = messageAdapter.getMessage(position)

            lifecycleScope.launch {
                with(messagesViewModel) { deleteMessage(deletedItem?.message?.id!!) }
            }
            val snackBar = Snackbar
                .make(
                    view!!,
                    "$name " + resources.getString(R.string.delete_message),
                    Snackbar.LENGTH_LONG
                )

            snackBar.setAction(resources.getString(R.string.undo)) {
                lifecycleScope.launch {
                    messagesViewModel.insertNewMessage(deletedItem?.message!!)
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rvConversation.smoothScrollToPosition(0)
                }, 300)
            }
            snackBar.setActionTextColor(Color.YELLOW)
            snackBar.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}