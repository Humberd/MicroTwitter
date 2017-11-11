package pl.bas.microtwitter.builders

import org.springframework.stereotype.Service
import pl.bas.microtwitter.dao.TweetDAO
import pl.bas.microtwitter.dto.TweetResponseDTO
import pl.bas.microtwitter.dto.buildTweetResponseDTO
import pl.bas.microtwitter.repositories.TweetLikeRepository
import pl.bas.microtwitter.repositories.TweetRepository

@Service
class ResponseBuilder(
        val tweetRepository: TweetRepository,
        val tweetLikeRepository: TweetLikeRepository) {

    fun buildTweetResponse(tweetDAO: TweetDAO): TweetResponseDTO {
        return buildTweetResponseDTO(tweetDAO, tweetLikeRepository)
    }
}