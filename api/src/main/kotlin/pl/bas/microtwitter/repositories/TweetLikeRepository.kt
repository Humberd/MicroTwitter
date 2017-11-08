package pl.bas.microtwitter.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.bas.microtwitter.dao.TweetDAO
import pl.bas.microtwitter.dao.TweetLikeDAO

interface TweetLikeRepository : JpaRepository<TweetLikeDAO, Long> {
    fun countByTweet(tweet: TweetDAO): Int
}