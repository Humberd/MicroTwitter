package pl.bas.microtwitter.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.bas.microtwitter.dao.TweetDAO
import pl.bas.microtwitter.dao.TweetLikeDAO
import pl.bas.microtwitter.dao.UserDAO

interface TweetLikeRepository : JpaRepository<TweetLikeDAO, Long> {
    fun countByTweet(tweet: TweetDAO): Int
    fun countByTweetAndUser(tweet: TweetDAO, userDAO: UserDAO): Int?
}