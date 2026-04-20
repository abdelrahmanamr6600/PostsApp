package com.intercom.posts.domain.repository

import com.intercom.posts.domain.model.PostDto
import com.intercom.posts.domain.model.PostResponseDto
import kotlinx.coroutines.flow.Flow

interface PostRepository {
      fun getPosts(userId: Int): Flow<PostResponseDto>
}