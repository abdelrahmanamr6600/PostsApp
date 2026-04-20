package com.intercom.posts.domain.repository

import com.intercom.posts.domain.model.UserDto
import kotlinx.coroutines.flow.Flow

interface UserRepository {

        fun getUsers(): Flow<List<UserDto>>
}