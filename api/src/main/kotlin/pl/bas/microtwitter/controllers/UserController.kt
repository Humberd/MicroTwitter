package pl.bas.microtwitter.controllers

import org.hibernate.collection.internal.PersistentBag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.bas.microtwitter.builders.ResponseBuilder
import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.dto.UserResponseDTO
import pl.bas.microtwitter.exceptions.BadRequestException
import pl.bas.microtwitter.repositories.UserRepository
import javax.transaction.Transactional

@RestController
@RequestMapping("/")
class UserController(
        val userRepository: UserRepository,
        val responseBuilder: ResponseBuilder) {

    @GetMapping("/me")
    fun getMe(user: UserDAO): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.ok(responseBuilder.buildUserResponse(user, privateResponse = true))
    }

    @GetMapping("/users")
    fun getUsers(@RequestParam username: String,
                 pageable: Pageable): ResponseEntity<Page<UserResponseDTO>> {
        if (username.isBlank()) {
            throw BadRequestException("Username must not by an empty string")
        }
        val page = userRepository.findAllByLcusernameContaining(username.toLowerCase(), pageable)

        return ResponseEntity.ok(page.map { user -> responseBuilder.buildUserResponse(user) })
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

    @Transactional
    @PostMapping("/users/{userId}/follow")
    fun followUser(@PathVariable userId: Long,
                   user: UserDAO): ResponseEntity<Unit> {
        if (userId == user.id) throw BadRequestException("Cannot follow myself")

        if (user.follows.find { userDAO -> userDAO.id == userId } !== null) {
            throw BadRequestException("You have already followed $userId")
        }

        val userToFollow = userRepository.findById(userId).let {
            if (!it.isPresent) throw BadRequestException("User does not exist")
            it.get()
        }

        (user.follows as PersistentBag).add(userToFollow)

        return ResponseEntity.ok(Unit)
    }

    @Transactional
    @PostMapping("/users/{userId}/unfollow")
    fun unfollowUser(@PathVariable userId: Long,
                   user: UserDAO): ResponseEntity<Unit> {
        if (userId == user.id) throw BadRequestException("Cannot unfollow myself")

        if (user.follows.find { userDAO -> userDAO.id == userId } === null) {
            throw BadRequestException("You are not following $userId")
        }

        val userToUnfollow = userRepository.findById(userId).let {
            if (!it.isPresent) throw BadRequestException("User does not exist")
            it.get()
        }

        (user.follows as PersistentBag).removeIf { userDAO -> (userDAO as UserDAO).id == userToUnfollow.id }

        return ResponseEntity.ok(Unit)
    }

}

