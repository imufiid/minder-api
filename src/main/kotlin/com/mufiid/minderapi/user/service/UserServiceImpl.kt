package com.mufiid.minderapi.user.service

import com.mufiid.minderapi.user.model.Mapper
import com.mufiid.minderapi.user.model.User
import com.mufiid.minderapi.user.model.UserRequest
import com.mufiid.minderapi.user.model.UserResponse
import com.mufiid.minderapi.user.repository.UserRepository
import com.mufiid.minderapi.utils.MinderException
import com.mufiid.minderapi.utils.ValidationUtils
import com.mufiid.minderapi.utils.toResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    @Autowired
    private val userRepository: UserRepository,

    @Autowired
    private val validationUtils: ValidationUtils
) : UserService {

    override fun create(userRequest: UserRequest): Result<UserResponse> {
        validationUtils.validate(userRequest)

        val existingUser = getByUsername(userRequest.userName)
        if (existingUser.isSuccess) {
            throw MinderException("User is exist!")
        }

        val user = User(
            userName = userRequest.userName,
            firstName = userRequest.firstName,
            lastName = userRequest.lastName,
            password = userRequest.password,
            createdAt = System.currentTimeMillis().toString()
        )
        val result =  userRepository.save(user)
        return Mapper.mapEntityToResponse(result).toResult()
    }

    override fun getByUsername(username: String): Result<User> {
        return userRepository.findBy(username).toResult()
    }
}