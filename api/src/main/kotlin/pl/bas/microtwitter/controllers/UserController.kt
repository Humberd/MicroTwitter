package pl.bas.microtwitter.controllers

import org.hibernate.collection.internal.PersistentBag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pl.bas.microtwitter.builders.ResponseBuilder
import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.dto.ProfileUpdateDTO
import pl.bas.microtwitter.dto.UserResponseDTO
import pl.bas.microtwitter.exceptions.BadRequestException
import pl.bas.microtwitter.repositories.UserRepository
import pl.bas.microtwitter.resolvers.Optional
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    fun getMe(user: UserDAO): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.ok(responseBuilder.buildUserResponse(user, user))
    }

    /**
     * Updates user profile
     */
    @PreAuthorize("isAuthenticated()")
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

        return ResponseEntity.ok(responseBuilder.buildUserResponse(user, user))
    }

    /**
     * Gets a paginated list of users selected by [username] or [profile.fullName]
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/users")
    fun getUsers(@RequestParam usernameOrfullName: String,
                 pageable: Pageable,
                 @Optional user: UserDAO?): ResponseEntity<Page<UserResponseDTO>> {
        if (usernameOrfullName.isBlank()) {
            throw BadRequestException("Username or FullName must be a not empty string")
        }
        val page = userRepository.findByUsernameOrFullName(usernameOrfullName.toLowerCase(), pageable)

        return ResponseEntity.ok(page.map { userDAO -> responseBuilder.buildUserResponse(user, userDAO) })
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/users/{username}/following-users")
    fun getFollowingUsers(@PathVariable username: String?,
                          pageable: Pageable,
                          @Optional user: UserDAO?): ResponseEntity<Page<UserResponseDTO>> {
        if (username.isNullOrBlank()) {
            throw BadRequestException("Username must be a not empty string")
        }
        val page = userRepository.findFollowedUsers(username!!.toLowerCase(), pageable)

        return ResponseEntity.ok(page.map { userDAO -> responseBuilder.buildUserResponse(user, userDAO) })
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/users/{username}/followers")
    fun getFollowers(@PathVariable username: String?,
                     pageable: Pageable,
                     @Optional user: UserDAO?): ResponseEntity<Page<UserResponseDTO>> {
        if (username.isNullOrBlank()) {
            throw BadRequestException("Username must be a not empty string")
        }
        val page = userRepository.findFollowers(username!!.toLowerCase(), pageable)

        return ResponseEntity.ok(page.map { userDAO -> responseBuilder.buildUserResponse(user, userDAO) })
    }

    /**
     * Gets a user info by [username]
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/users/{username}")
    fun getUser(@PathVariable username: String,
                @Optional user: UserDAO?): ResponseEntity<UserResponseDTO> {
        val selectedUser = if (user?.lcusername == username.toLowerCase()) {
            user
        } else {
            userRepository.findByLcusername(username.toLowerCase()).apply {
                if (this === null) throw BadRequestException("User does not exist")
            }!!
        }

        return ResponseEntity.ok(responseBuilder.buildUserResponse(user, selectedUser))
    }

    /**
     * Follows a user
     * If user x follows user y, then user x will receive user y tweets on his wall
     */
    @PreAuthorize("isAuthenticated()")
    @Transactional
    @PostMapping("/users/{userId}/follow")
    fun followUser(@PathVariable userId: Long,
                   user: UserDAO): ResponseEntity<UserResponseDTO> {
        if (userId == user.id) throw BadRequestException("Cannot follow myself")

        if (user.followedUsers.find { userDAO -> userDAO.id == userId } !== null) {
            throw BadRequestException("You have already followed $userId")
        }

        val userToFollow = userRepository.findById(userId).let {
            if (!it.isPresent) throw BadRequestException("User does not exist")
            it.get()
        }

        (user.followedUsers as PersistentBag).add(userToFollow)

        return ResponseEntity.ok(responseBuilder.buildUserResponse(user, userToFollow))
    }

    /**
     * Unfollows a user
     *
     */
    @PreAuthorize("isAuthenticated()")
    @Transactional
    @PostMapping("/users/{userId}/unfollow")
    fun unfollowUser(@PathVariable userId: Long,
                     user: UserDAO): ResponseEntity<UserResponseDTO> {
        if (userId == user.id) throw BadRequestException("Cannot unfollow myself")

        if (user.followedUsers.find { userDAO -> userDAO.id == userId } === null) {
            throw BadRequestException("You are not following $userId")
        }

        val userToUnfollow = userRepository.findById(userId).let {
            if (!it.isPresent) throw BadRequestException("User does not exist")
            it.get()
        }

        (user.followedUsers as PersistentBag).removeIf { userDAO -> (userDAO as UserDAO).id == userToUnfollow.id }

        return ResponseEntity.ok(responseBuilder.buildUserResponse(user, userToUnfollow))
    }

}

