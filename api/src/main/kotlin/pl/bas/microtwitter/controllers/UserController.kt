package pl.bas.microtwitter.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.bas.microtwitter.builders.ResponseBuilder
import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.dto.UserResponseDTO
import pl.bas.microtwitter.exceptions.BadRequestException
import pl.bas.microtwitter.repositories.UserRepository

@RestController
@RequestMapping("/")
class UserController(
        val userRepository: UserRepository,
        val responseBuilder: ResponseBuilder) {

    @GetMapping("/me")
    fun getMe(user: UserDAO): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.ok(responseBuilder.buildUserResponse(user, privateResponse = true))
    }

    @GetMapping("/users/{username}")
    fun getUser(@PathVariable username: String,
                user: UserDAO): ResponseEntity<UserResponseDTO> {
        val selectedUser = if (user.lcusername == username.toLowerCase()) {
            user
        } else {
            userRepository.findByLcusername(username.toLowerCase()).apply {
                if (this === null) throw BadRequestException("User does not exist")
            }!!
        }

        return ResponseEntity.ok(responseBuilder.buildUserResponse(
                selectedUser,
                privateResponse = selectedUser === user))
    }
}

