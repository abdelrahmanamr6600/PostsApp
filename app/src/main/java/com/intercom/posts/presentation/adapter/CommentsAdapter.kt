package com.intercom.posts.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intercom.posts.databinding.ItemCommentBinding
import com.intercom.posts.domain.model.CommentDto

/**
 * Adapter for displaying comments in RecyclerView
 * Uses DiffUtil for efficient list updates
 * Displays comment body, user information, and likes count
 */
class CommentsAdapter : ListAdapter<CommentDto, CommentsAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder for individual comment items
     */
    class CommentViewHolder(
        private val binding: ItemCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: CommentDto) {
            with(binding) {
                // Display comment body
                commentBodyTextView.text = comment.body

                // Display user information
                commentUserNameTextView.text = comment.user.fullName
                commentUserUsernameTextView.text = "@${comment.user.username}"

                // Display likes count
                commentLikesTextView.text = "${comment.likes} likes"
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    private class CommentDiffCallback : DiffUtil.ItemCallback<CommentDto>() {
        override fun areItemsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CommentDto, newItem: CommentDto): Boolean {
            return oldItem == newItem
        }
    }
}

