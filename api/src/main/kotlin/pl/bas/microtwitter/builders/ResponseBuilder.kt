package pl.bas.microtwitter.builders

import org.springframework.stereotype.Service
import pl.bas.microtwitter.dao.TweetDAO
import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.dto.TweetResponseDTO
import pl.bas.microtwitter.dto.UserResponseDTO
import pl.bas.microtwitter.dto.buildTweetResponseDTO
import pl.bas.microtwitter.dto.buildUserResponseDTO
import pl.bas.microtwitter.repositories.TweetLikeRepository
import pl.bas.microtwitter.repositories.TweetRepository
import pl.bas.microtwitter.repositories.UserRepository

@Service
class ResponseBuilder(
        val tweetRepository: TweetRepository,
        val tweetLikeRepository: TweetLikeRepository,
        val userRepository: UserRepository) {

    fun buildTweetResponse(me: UserDAO, tweetDAO: TweetDAO): TweetResponseDTO {
        return buildTweetResponseDTO(me, tweetDAO, tweetLikeRepository, tweetRepository)
    }

    fun buildUserResponse(me: UserDAO, user: UserDAO, privateResponse: Boolean = false): UserResponseDTO {
        return buildUserResponseDTO(me, user, userRepository, privateResponse)
    }
}