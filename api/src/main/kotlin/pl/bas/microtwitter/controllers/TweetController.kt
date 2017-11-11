package pl.bas.microtwitter.controllers

import mu.KLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.bas.microtwitter.builders.ResponseBuilder
import pl.bas.microtwitter.dao.TweetDAO
import pl.bas.microtwitter.dao.TweetLikeDAO
import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.dto.TweetCreateDTO
import pl.bas.microtwitter.dto.TweetResponseDTO
import pl.bas.microtwitter.dto.buildTweetResponseDTO
import pl.bas.microtwitter.exceptions.BadRequestException
import pl.bas.microtwitter.repositories.TweetLikeRepository
import pl.bas.microtwitter.repositories.TweetRepository
import javax.transaction.Transactional

@RestController
@RequestMapping("/tweets")
class TweetController(
        val tweetRepository: TweetRepository,
        val tweetLikeRepository: TweetLikeRepository,
        val responseBuilder: ResponseBuilder) {
    companion object : KLogging()

    @PostMapping("/")
    fun createTweet(@RequestBody body: TweetCreateDTO,
                    user: UserDAO): ResponseEntity<TweetResponseDTO> {
        val tweetData = TweetDAO().apply {
            content = body.content
            this.user = user
        }
        val tweet = tweetRepository.save(tweetData)

        return ResponseEntity.ok(responseBuilder.buildTweetResponse(tweet))
    }

    @Transactional
    @PostMapping("/{tweetId}/likes")
    fun likeTweet(@PathVariable tweetId: Long,
                  user: UserDAO): ResponseEntity<TweetResponseDTO> {
        val tweet = tweetRepository.findById(tweetId).let {
            if (!it.isPresent) {
                throw BadRequestException("Cannot find a tweet with id '$tweetId'")
            }
            it.get()
        }

        tweet.likes.find {
            it?.user?.id === user.id
        }.apply {
            if (this !== null) {
                throw BadRequestException("Already liked")
            }
        }

        val tweetLike = TweetLikeDAO().apply {
            this.tweet = tweet
            this.user = user
        }

        tweetLikeRepository.save(tweetLike)

        return ResponseEntity.ok(responseBuilder.buildTweetResponse(tweet))
    }

    @GetMapping("/")
    fun getTweets(@RequestParam username: String,
                  pageable: Pageable): ResponseEntity<Page<TweetResponseDTO>> {
        val page = tweetRepository.findAllByUserLcusername(username.toLowerCase(), pageable)
        return ResponseEntity.ok(page.map { tweet -> responseBuilder.buildTweetResponse(tweet) })
    }
}