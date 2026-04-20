package com.intercom.posts.domain.usecase

import com.intercom.posts.domain.repository.CommentsRepository

class GetPostCommentsUseCase(private val repository: CommentsRepository) {

    operator fun invoke(postId: Int) = repository.getComments(postId)
}