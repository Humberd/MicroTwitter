package pl.bas.microtwitter.controllers

import org.hibernate.collection.internal.PersistentBag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.bas.microtwitter.builders.ResponseBuilder
import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.dto.ProfileUpdateDTO
import pl.bas.microtwitter.dto.UserResponseDTO
import pl.bas.microtwitter.exceptions.BadRequestException
import pl.bas.microtwitter.repositories.UserRepository
import javax.transaction.Transactional

@CrossOrigin
@RestController
@RequestMapping("/")
class UserController(
        val userRepository: UserRepository,
        val responseBuilder: ResponseBuilder) {

    /**
     * Gets logged in user info
     */
    @GetMapping("/me")
    fun getMe(user: UserDAO): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.ok(responseBuilder.buildUserResponse(user, user, privateResponse = true))
    }

    /**
     * Updates user profile
     */
    @Transactional
    @PutMapping("/me/profile")
    fun updateProfile(@RequestBody body: ProfileUpdateDTO,
                      user: UserDAO): ResponseEntity<UserResponseDTO> {
        user.profile?.apply {
            fullName = body.fullName
            description = body.description
            location = body.location
            profileLinkColor = body.profileLinkColor
            url = body.url
            birthdate!!.apply {
                day = body.birthdate?.day
                month = body.birthdate?.month
                year = body.birthdate?.year
            }
        }

        return ResponseEntity.ok(responseBuilder.buildUserResponse(user, user, privateResponse = true))
    }

    /**
     * Gets a paginated list of users selected by [username]
     */
    @GetMapping("/users")
    fun getUsers(@RequestParam username: String,
                 pageable: Pageable,
                 user: UserDAO): ResponseEntity<Page<UserResponseDTO>> {
        if (username.isBlank()) {
            throw BadRequestException("Username must not by an empty string")
        }
        val page = userRepository.findAllByLcusernameContaining(username.toLowerCase(), pageable)

        return ResponseEntity.ok(page.map { userDAO -> responseBuilder.buildUserResponse(user, userDAO) })
    }

    /**
     * Gets a user info by [username]
     */
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
                me = user,
                user = selectedUser,
                privateResponse = selectedUser === user))
    }

    /**
     * Follows a user
     * If user x follows user y, then user x will receive user y tweets on his wall
     */
    @Transactional
    @PostMapping("/users/{userId}/follow")
    fun followUser(@PathVariable userId: Long,
                   user: UserDAO): ResponseEntity<Unit> {
        if (userId == user.id) throw BadRequestException("Cannot follow myself")

        if (user.followedUsers.find { userDAO -> userDAO.id == userId } !== null) {
            throw BadRequestException("You have already followed $userId")
        }

        val userToFollow = userRepository.findById(userId).let {
            if (!it.isPresent) throw BadRequestException("User does not exist")
            it.get()
        }

        (user.followedUsers as PersistentBag).add(userToFollow)

        return ResponseEntity.ok(Unit)
    }

    /**
     * Unfollows a user
     *
     */
    @Transactional
    @PostMapping("/users/{userId}/unfollow")
    fun unfollowUser(@PathVariable userId: Long,
                     user: UserDAO): ResponseEntity<Unit> {
        if (userId == user.id) throw BadRequestException("Cannot unfollow myself")

        if (user.followedUsers.find { userDAO -> userDAO.id == userId } === null) {
            throw BadRequestException("You are not following $userId")
        }

        val userToUnfollow = userRepository.findById(userId).let {
            if (!it.isPresent) throw BadRequestException("User does not exist")
            it.get()
        }

        (user.followedUsers as PersistentBag).removeIf { userDAO -> (userDAO as UserDAO).id == userToUnfollow.id }

        return ResponseEntity.ok(Unit)
    }

}

