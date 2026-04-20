package com.intercom.posts.data.remote.api


import com.intercom.posts.domain.model.CommentsResponseDto
import com.intercom.posts.domain.model.PostResponseDto
import com.intercom.posts.domain.model.UsersResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("posts/user/{id}")
    suspend fun getPosts(@Path("id") userId: Int): PostResponseDto

    @GET("users")
    suspend fun getUsers(): UsersResponseDto

    @GET("posts/{id}/comments")
    suspend fun getComments(@Path("id") postId: Int): CommentsResponseDto
}