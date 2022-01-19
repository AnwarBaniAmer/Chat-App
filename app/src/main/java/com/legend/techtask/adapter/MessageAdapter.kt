package com.legend.techtask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import com.legend.techtask.R
import com.legend.techtask.databinding.RowMessageReceivedBinding
import com.legend.techtask.databinding.RowMessageSentBinding
import com.legend.techtask.model.MessagesAndUsers
import java.lang.Exception

class MessageAdapter(private val mContext: Context) :
    PagingDataAdapter<MessagesAndUsers, RecyclerView.ViewHolder>(differCallback) {

    enum class ViewType(val type: Int) {
        VIEW_TYPE_MESSAGE_SENT(1),
        VIEW_TYPE_MESSAGE_RECEIVED(2),
    }

    private var lastPosition = -1

    companion object {

        private val differCallback = object : DiffUtil.ItemCallback<MessagesAndUsers>() {
            override fun areItemsTheSame(
                oldItem: MessagesAndUsers,
                newItem: MessagesAndUsers
            ): Boolean =
                oldItem.message.id == newItem.message.id

            override fun areContentsTheSame(
                oldItem: MessagesAndUsers,
                newItem: MessagesAndUsers
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message: MessagesAndUsers? = getItem(position)

        return if (message?.message?.isSender() == true)
            ViewType.VIEW_TYPE_MESSAGE_SENT.type
         else
            ViewType.VIEW_TYPE_MESSAGE_RECEIVED.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            ViewType.VIEW_TYPE_MESSAGE_SENT.type -> {
                return SentMessageHolder(
                    RowMessageSentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            ViewType.VIEW_TYPE_MESSAGE_RECEIVED.type -> {
                return ReceivedMessageHolder(
                    RowMessageReceivedBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw Exception("invalid view holder ")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        animateItems(holder)

        val messagesAndUsers: MessagesAndUsers? = getItem(holder.bindingAdapterPosition)

        when (holder.itemViewType) {

            ViewType.VIEW_TYPE_MESSAGE_SENT.type -> (holder as SentMessageHolder).itemView.apply {
                if (messagesAndUsers != null) {
                    holder.bind(messagesAndUsers)
                }
            }

            ViewType.VIEW_TYPE_MESSAGE_RECEIVED.type -> (holder as ReceivedMessageHolder).itemView.apply {
                if (messagesAndUsers != null) {
                    holder.bind(messagesAndUsers)
                }
            }
        }
    }


    inner class SentMessageHolder(val sentMessageBinding: RowMessageSentBinding) :
        RecyclerView.ViewHolder(sentMessageBinding.root) {

        fun bind(messagesAndUsers: MessagesAndUsers) {
            sentMessageBinding.txtMessage.text = messagesAndUsers.message.content
            val sentAttachmentAdapter = AttachmentAdapter(mContext)
                sentMessageBinding.rvMsgSentAttachment.apply {
                    adapter = sentAttachmentAdapter
                }
                sentAttachmentAdapter.differ.submitList(messagesAndUsers.message.attachments)
        }
    }

    inner class ReceivedMessageHolder(val binding: RowMessageReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(messagesAndUsers: MessagesAndUsers) {
            val receivedAttachmentAdapter = AttachmentAdapter(mContext)

            binding.txtMessage.text = messagesAndUsers.message.content
            binding.txtUserName.text = messagesAndUsers.user.name

            Glide.with(mContext)
                .load(messagesAndUsers.user.avatarId)
                .error(R.drawable.ic_baseline_account_circle_24)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(binding.imgUserAvatar)

                binding.rvMsgReceivedAttachment.apply {
                    adapter = receivedAttachmentAdapter
                }
                receivedAttachmentAdapter.differ.submitList(messagesAndUsers.message.attachments)
        }
    }

    private fun animateItems(holder: RecyclerView.ViewHolder) {
        val animation: Animation = AnimationUtils.loadAnimation(
            mContext,
            if (holder.bindingAdapterPosition > lastPosition) R.anim.up_from_bottom else R.anim.down_from_top
        )
        holder.itemView.startAnimation(animation)
        lastPosition = holder.bindingAdapterPosition
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.clearAnimation()
    }

    fun getMessage(position: Int): MessagesAndUsers? {
        return getItem(position)
    }
}