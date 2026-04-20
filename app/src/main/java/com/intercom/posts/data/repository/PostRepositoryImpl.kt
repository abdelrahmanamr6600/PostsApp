package com.intercom.posts.data.repository

import com.intercom.posts.data.remote.api.ApiService
import com.intercom.posts.data.remote.network.RetrofitInstance
import com.intercom.posts.domain.model.PostDto
import com.intercom.posts.domain.model.PostResponseDto
import com.intercom.posts.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostRepositoryImpl( private val api: ApiService) : PostRepository {
    override  fun  getPosts(userId: Int): Flow<PostResponseDto>  = flow {
        val posts = api.getPosts(userId)
        emit(posts)
    }


}