package com.legend.techtask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.legend.techtask.R
import com.legend.techtask.databinding.RowItemAttachmentBinding
import com.legend.techtask.model.Attachment
import com.legend.techtask.utils.Constants.Companion.ATTACHMENTS_PLACEHOLDERS

class AttachmentAdapter(val mContext: Context) :
    RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Attachment>() {
        override fun areItemsTheSame(oldItem: Attachment, newItem: Attachment): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Attachment, newItem: Attachment): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder =
        AttachmentViewHolder(
            RowItemAttachmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        val attachment = differ.currentList[position]
        if (attachment != null)
            holder.bind(attachment)
    }

    override fun getItemCount(): Int =
        differ.currentList.size


    inner class AttachmentViewHolder(val binding: RowItemAttachmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(attachment: Attachment) {
            binding.imgThumbnail.visibility = View.VISIBLE
            Glide.with(mContext)
                .load(attachment.thumbnailUrl + "\\" + attachment.id)
                .thumbnail(
                    Glide
                        .with(mContext)
                        .load(ATTACHMENTS_PLACEHOLDERS + (1 until 1000).random())
                )
                .error(R.drawable.ic_baseline_image_200)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(binding.imgThumbnail)
            binding.txtAttachmentTitle.text = attachment.title
        }
    }
}