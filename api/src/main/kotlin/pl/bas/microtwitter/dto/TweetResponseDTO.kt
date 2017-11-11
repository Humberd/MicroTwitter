package pl.bas.microtwitter.dto

import pl.bas.microtwitter.dao.TweetDAO
import pl.bas.microtwitter.repositories.TweetLikeRepository
import pl.bas.microtwitter.repositories.TweetRepository
import java.util.*

data class TweetResponseDTO(
        var id: Long?,
        var content: String?,
        var createdAt: Date?,
        var likesCount: Int?,
        var commentsCount: Int?,
        var user: TweetUserResponseDTO?
)

data class TweetUserResponseDTO(
        var id: Long?,
        var username: String?
)


fun buildTweetResponseDTO(tweet: TweetDAO,
                          tweetLikeRepository: TweetLikeRepository,
                          tweetRepository: TweetRepository): TweetResponseDTO {
    return TweetResponseDTO(
            id = tweet.id,
            content = tweet.content,
            createdAt = tweet.createdAt,
            likesCount = tweetLikeRepository.countByTweet(tweet),
            commentsCount = tweetRepository.countByInReplyToTweet(tweet),
            user = TweetUserResponseDTO(
                    id = tweet.user?.id,
                    username = tweet.user?.username
            )
    )
}