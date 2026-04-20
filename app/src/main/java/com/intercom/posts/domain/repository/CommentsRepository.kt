package com.intercom.posts.domain.repository

import com.intercom.posts.domain.model.CommentDto
import com.intercom.posts.domain.model.CommentsResponseDto
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {

   fun  getComments(postId: Int) : Flow<CommentsResponseDto>
}