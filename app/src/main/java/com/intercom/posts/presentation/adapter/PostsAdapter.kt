package com.intercom.posts.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intercom.posts.databinding.ItemPostBinding
import com.intercom.posts.domain.model.PostDto

/**
 * Adapter for displaying posts in RecyclerView
 * Uses DiffUtil for efficient list updates
 */
class PostsAdapter(
    private val onPostClick: (PostDto) -> Unit
) : ListAdapter<PostDto, PostsAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding, onPostClick)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder for individual post items
     */
    class PostViewHolder(
        private val binding: ItemPostBinding,
        private val onPostClick: (PostDto) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostDto) {
            with(binding) {
                postTitleTextView.text = post.title
                postBodyTextView.text = post.body

                // Handle item click
                root.setOnClickListener {
                    onPostClick(post)
                }
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    private class PostDiffCallback : DiffUtil.ItemCallback<PostDto>() {
        override fun areItemsTheSame(oldItem: PostDto, newItem: PostDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostDto, newItem: PostDto): Boolean {
            return oldItem == newItem
        }
    }
}

