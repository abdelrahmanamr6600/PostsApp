package com.intercom.posts.data.repository

import com.intercom.posts.data.remote.api.ApiService
import com.intercom.posts.domain.model.UserDto
import com.intercom.posts.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl( private val api: ApiService) : UserRepository {
    override fun getUsers(): Flow<List<UserDto>>  = flow {
        val users = api.getUsers()
        emit(users.users)
    }


}