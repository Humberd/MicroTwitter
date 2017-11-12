package pl.bas.microtwitter.dto

import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.repositories.UserRepository
import java.util.*

data class UserResponseDTO(
        var id: Long?,
        var createdAt: Date?,
        var username: String?,
        var email: String?,
        var fullName: String?,
        var tweetsCount: Int?,
        var likesCount: Int?,
        var followsCount: Int?,
        var followedByCount: Int?
//        var isFollowing: Boolean,
)

fun buildUserResponseDTO(user: UserDAO,
                         userRepository: UserRepository,
                         privateResponse: Boolean = false): UserResponseDTO {
    return UserResponseDTO(
            id = user.id,
            createdAt = if (privateResponse) user.createdAt else null,
            username = user.username,
            email = if (privateResponse) user.email else null,
            fullName = user.fullName,
            tweetsCount = userRepository.countByTweets_User(user),
            likesCount = userRepository.countByLikes_User(user),
            followsCount = userRepository.countByFollows_IsFollowedBy(user),
            followedByCount = userRepository.countByIsFollowedBy_Follows(user)
    )
}