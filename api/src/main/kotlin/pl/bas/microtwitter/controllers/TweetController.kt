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

    /**
     * Creates a tweet and returns its new instance
     */
    @PostMapping("/")
    fun createTweet(@RequestBody body: TweetCreateDTO,
                    user: UserDAO): ResponseEntity<TweetResponseDTO> {
        val tweetData = TweetDAO().apply {
            content = body.content
            this.user = user

            if (body.inReplyToTweetId !== null) {
                inReplyToTweet = tweetRepository.findById(body.inReplyToTweetId!!)
                        .orElseThrow { throw BadRequestException("inReplyToTweet does not exist") }
            }
        }
        val tweet = tweetRepository.save(tweetData)

        return ResponseEntity.ok(responseBuilder.buildTweetResponse(tweet))
    }

    /**
     * Gets a paginated list of tweets by given [username]
     */
    @GetMapping("/")
    fun getTweets(@RequestParam username: String,
                  pageable: Pageable): ResponseEntity<Page<TweetResponseDTO>> {
        val page = tweetRepository.findAllByUserLcusername(username.toLowerCase(), pageable)

        return ResponseEntity.ok(page.map { tweet -> responseBuilder.buildTweetResponse(tweet) })
    }

    /**
     * Gets a tweet by [tweetId]
     */
    @GetMapping("/{tweetId}")
    fun getTweet(@PathVariable tweetId: Long): ResponseEntity<TweetResponseDTO> {
        val tweet = tweetRepository.findById(tweetId).let {
            if (!it.isPresent) throw BadRequestException("Cannot find a tweet with id '$tweetId'")
            it.get()
        }

        return ResponseEntity.ok(responseBuilder.buildTweetResponse(tweet))
    }

    /**
     * Deletes a tweet by [tweetId] only by a tweet creator
     */
    @Transactional
    @DeleteMapping("/{tweetId}")
    fun deleteTweet(@PathVariable tweetId: Long): ResponseEntity<Unit> {
        val tweet = tweetRepository.findById(tweetId).let {
            if (!it.isPresent) throw BadRequestException("Cannot find a tweet with id '$tweetId'")
            it.get()
        }
        tweetRepository.delete(tweet)

        return ResponseEntity.ok(Unit)
    }

    /**
     * Adds a like to a tweet.
     * Likes from user are unique per tweet
     */
    @Transactional
    @PostMapping("/{tweetId}/likes")
    fun likeTweet(@PathVariable tweetId: Long,
                  user: UserDAO): ResponseEntity<TweetResponseDTO> {
        val tweet = tweetRepository.findById(tweetId).let {
            if (!it.isPresent) throw BadRequestException("Cannot find a tweet with id '$tweetId'")
            it.get()
        }

        tweet.likes.find {
            it?.user?.id === user.id
        }.apply {
            if (this !== null) throw BadRequestException("Already liked")
        }

        val tweetLike = TweetLikeDAO().apply {
            this.tweet = tweet
            this.user = user
        }

        println(tweet.toString())

        tweetLikeRepository.save(tweetLike)

        return ResponseEntity.ok(responseBuilder.buildTweetResponse(tweet))
    }

    @GetMapping("/{tweetId}/comments")
    fun getComments(@PathVariable tweetId: Long,
                    pageable: Pageable): ResponseEntity<Page<TweetResponseDTO>> {
        val baseTweet = tweetRepository.findById(tweetId).let {
            if (!it.isPresent) {
                throw BadRequestException("Tweet does not exist")
            }
            it.get()
        }
        val page = tweetRepository.findAllByInReplyToTweet(baseTweet, pageable)

        return ResponseEntity.ok(page. map { tweet -> responseBuilder.buildTweetResponse(tweet) })
    }
}