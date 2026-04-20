package com.intercom.posts.domain.model

data class PostDto(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
    val tags: List<String>,
    val reactions: ReactionsDto,
    val views: Int
)