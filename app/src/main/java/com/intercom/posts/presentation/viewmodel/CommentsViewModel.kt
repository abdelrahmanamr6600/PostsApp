package com.intercom.posts.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intercom.posts.data.remote.network.RetrofitInstance
import com.intercom.posts.data.repository.CommentsRepositoryImpl
import com.intercom.posts.domain.model.CommentsResponseDto
import com.intercom.posts.domain.model.PostResponseDto
import com.intercom.posts.domain.usecase.GetPostCommentsUseCase
import com.intercom.posts.presentation.uiState.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentsViewModel : ViewModel() {
    private val getPostCommentsUseCase : GetPostCommentsUseCase
    private val _state =
        MutableStateFlow<UiState<CommentsResponseDto>>(UiState.Loading())
    val state: StateFlow<UiState<CommentsResponseDto>> = _state


    init {
        val apiService  = RetrofitInstance.provideApiService()
        val commentsRepository = CommentsRepositoryImpl(apiService)
        getPostCommentsUseCase = GetPostCommentsUseCase(commentsRepository)
    }

        fun getComments(postId: Int) {
            viewModelScope.launch {
                getPostCommentsUseCase(postId).collect { response ->
                    if (response.comments.isEmpty()) {
                        _state.value = UiState.Empty()
                    } else {
                        _state.value = UiState.Success(response)
                    }
                }
            }
        }

}