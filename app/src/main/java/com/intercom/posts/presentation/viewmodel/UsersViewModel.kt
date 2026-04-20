package com.intercom.posts.presentation.viewmodel

import com.intercom.posts.presentation.uiState.UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intercom.posts.data.remote.network.RetrofitInstance
import com.intercom.posts.data.repository.UserRepositoryImpl
import com.intercom.posts.domain.model.UserDto
import com.intercom.posts.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {

    private val getUsersUseCase: GetUsersUseCase

    private val _state =
        MutableStateFlow<UiState<List<UserDto>>>(UiState.Loading())

    val state: StateFlow<UiState<List<UserDto>>> = _state

    init {
        val repository = UserRepositoryImpl(RetrofitInstance.provideApiService())
        getUsersUseCase = GetUsersUseCase(repository)
        getUsers()
    }

    fun getUsers() {
        viewModelScope.launch {

            _state.value = UiState.Loading()

            try {
                getUsersUseCase().collect { users ->
                    _state.value = UiState.Success(users)
                }
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Error")
            }
        }
    }
}