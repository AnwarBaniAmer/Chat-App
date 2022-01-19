package com.legend.techtask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.legend.techtask.R
import com.legend.techtask.databinding.RowUserBinding
import com.legend.techtask.model.UsersAndMessages

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<UsersAndMessages>() {
        override fun areItemsTheSame(
            oldItem: UsersAndMessages,
            newItem: UsersAndMessages
        ): Boolean = oldItem.user.id == newItem.user.id

        override fun areContentsTheSame(
            oldItem: UsersAndMessages,
            newItem: UsersAndMessages
        ): Boolean = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class UsersViewHolder(val binding: RowUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            RowUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = differ.currentList[position]
        holder.itemView.apply {
            holder.binding.txtUserName.text = user.user.name
            holder.binding.txtMessage.text = user.messagesList.last().content

            Glide.with(this)
                .load(user.user.avatarId)
                .error(R.drawable.ic_baseline_account_circle_24)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(holder.binding.userAvatar)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

}