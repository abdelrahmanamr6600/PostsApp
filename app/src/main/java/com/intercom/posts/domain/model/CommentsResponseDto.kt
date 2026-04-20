package com.intercom.posts.domain.model

data class CommentsResponseDto(
    val comments: List<CommentDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
