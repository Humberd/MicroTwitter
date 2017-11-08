package pl.bas.microtwitter.dto

import pl.bas.microtwitter.dao.TweetDAO
import pl.bas.microtwitter.repositories.TweetLikeRepository
import java.util.*

data class TweetResponseDTO(
        var id: Long?,
        var content: String?,
        var createdAt: Date?,
        var likes: Int?,
        var user: TweetUserResponseDTO?
)

data class TweetUserResponseDTO(
        var id: Long?,
        var username: String?
)

fun buildTweetResponseDTO(tweet: TweetDAO, tweetLikeRepository: TweetLikeRepository): TweetResponseDTO {
    return TweetResponseDTO(
            id = tweet.id,
            content = tweet.content,
            createdAt = tweet.createdAt,
            likes = tweetLikeRepository.countByTweet(tweet),
            user = TweetUserResponseDTO(
                    id = tweet.user?.id,
                    username = tweet.user?.username
            )
    )
}