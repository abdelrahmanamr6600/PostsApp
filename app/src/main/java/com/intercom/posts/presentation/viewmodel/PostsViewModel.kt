package com.intercom.posts.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intercom.posts.data.remote.network.RetrofitInstance
import com.intercom.posts.data.repository.CommentsRepositoryImpl
import com.intercom.posts.data.repository.PostRepositoryImpl
import com.intercom.posts.domain.model.PostResponseDto
import com.intercom.posts.domain.usecase.GetPostCommentsUseCase
import com.intercom.posts.domain.usecase.GetPostsUseCase
import com.intercom.posts.domain.usecase.GetUsersUseCase
import com.intercom.posts.presentation.uiState.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class PostsViewModel: ViewModel() {
    private val getPostsUseCase: GetPostsUseCase
    private val _state =
        MutableStateFlow<UiState<PostResponseDto>>(UiState.Loading())
    val state: StateFlow<UiState<PostResponseDto>> = _state


    init {
        val apiService  = RetrofitInstance.provideApiService()
        val postsRepository = PostRepositoryImpl(apiService)
        getPostsUseCase = GetPostsUseCase(postsRepository)

    }

     fun getPosts(userId: Int) {
        viewModelScope.launch {
            getPostsUseCase(userId).collect { response ->
                if (response.posts.isEmpty()) {
                    _state.value = UiState.Empty()
                } else {
                    _state.value = UiState.Success(response)
                    Log.d("PostsViewModel", "Posts fetched successfully: ${response.posts.size} posts")
                }
            }
        }
    }


}