package com.intercom.posts.domain.usecase

import com.intercom.posts.domain.model.UserDto
import com.intercom.posts.domain.repository.UserRepository


class GetUsersUseCase(private val userRepository: UserRepository) {

    operator fun invoke() : kotlinx.coroutines.flow.Flow<List<UserDto>> {
        return userRepository.getUsers()
    }
}