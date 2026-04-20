package com.intercom.posts.data.repository

import com.intercom.posts.data.remote.api.ApiService
import com.intercom.posts.domain.model.CommentDto
import com.intercom.posts.domain.model.CommentsResponseDto
import com.intercom.posts.domain.repository.CommentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CommentsRepositoryImpl(private val apiService: ApiService) : CommentsRepository {
    override fun getComments(postId: Int): Flow<CommentsResponseDto>  = flow {
        val comments = apiService.getComments(postId)
        emit(comments)
    }

}