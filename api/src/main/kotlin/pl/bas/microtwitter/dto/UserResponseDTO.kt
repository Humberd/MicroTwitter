package pl.bas.microtwitter.dto

import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.repositories.UserRepository
import java.util.*

data class UserResponseDTO(
        var id: Long?,
        var createdAt: Date?,
        var username: String?,
        var email: String?,
        var tweetsCount: Int?,
        var likesCount: Int?,
        var followsCount: Int?,
        var followedByCount: Int?,
        // does user follow user in this object
        var isFollowing: Boolean?,
        var profile: ProfileResponseDTO?
)

data class ProfileResponseDTO(
        var fullName: String?,
        var description: String?,
        var location: String?,
        var profileLinkColor: String?,
        var url: String?,
        var birthdate: BirthdateResponseDTO
)

data class BirthdateResponseDTO(
        var day: Short?,
        var month: Short?,
        var year: Short?
)

fun buildUserResponseDTO(me: UserDAO,
                         user: UserDAO,
                         userRepository: UserRepository,
                         privateResponse: Boolean = false): UserResponseDTO {
    return UserResponseDTO(
            id = user.id,
            createdAt = if (privateResponse) user.createdAt else null,
            username = user.username,
            email = if (privateResponse) user.email else null,
            tweetsCount = userRepository.countByTweets_User(user),
            likesCount = userRepository.countByLikes_User(user),
            followsCount = userRepository.countByFollows_IsFollowedBy(user),
            followedByCount = userRepository.countByIsFollowedBy_Follows(user),
            isFollowing = if (me.id == user.id) null
            else me.follows.find { followingUser -> followingUser.id == user.id } !== null,
            profile = ProfileResponseDTO(
                    fullName = user.profile!!.fullName,
                    description = user.profile!!.description,
                    location = user.profile!!.location,
                    profileLinkColor = user.profile!!.profileLinkColor,
                    url = user.profile!!.url,
                    birthdate = BirthdateResponseDTO(
                            day = user.profile!!.birthdate?.day,
                            month = user.profile!!.birthdate?.month,
                            year = user.profile!!.birthdate?.year
                    )
            )
    )
}