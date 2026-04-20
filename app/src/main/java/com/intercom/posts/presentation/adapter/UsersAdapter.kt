package com.intercom.posts.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intercom.posts.R
import com.intercom.posts.databinding.ItemUserBinding
import com.intercom.posts.domain.model.UserDto

/**
 * Adapter for displaying users in RecyclerView
 * Uses DiffUtil for efficient list updates
 */
class UsersAdapter(
    private val onUserClick: (UserDto) -> Unit
) : ListAdapter<UserDto, UsersAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding, onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder for individual user items
     */
    class UserViewHolder(
        private val binding: ItemUserBinding,
        private val onUserClick: (UserDto) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserDto) {
            with(binding) {
                userNameTextView.text = "${user.firstName} ${user.lastName}"
                
                // Load user image using Glide
                Glide.with(itemView.context)
                    .load(user.image)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(userImageView)

                // Handle item click
                root.setOnClickListener {
                    onUserClick(user)
                }
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    private class UserDiffCallback : DiffUtil.ItemCallback<UserDto>() {
        override fun areItemsTheSame(oldItem: UserDto, newItem: UserDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserDto, newItem: UserDto): Boolean {
            return oldItem == newItem
        }
    }
}

