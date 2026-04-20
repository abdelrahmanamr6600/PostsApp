package com.intercom.posts.domain.usecase

import com.intercom.posts.data.repository.PostRepositoryImpl
import com.intercom.posts.domain.model.PostDto
import com.intercom.posts.domain.model.PostResponseDto
import com.intercom.posts.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class GetPostsUseCase(
    private val repository: PostRepository
) {

    operator fun invoke(userId: Int): Flow<PostResponseDto> {
        return repository.getPosts(userId)
    }
}