package com.intercom.posts.domain.model

data class CommentDto(
    val id: Int,
    val body: String,
    val postId: Int,
    val likes: Int,
    val user: CommentUserDto
)
