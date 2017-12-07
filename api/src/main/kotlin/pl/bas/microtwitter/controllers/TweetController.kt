package pl.bas.microtwitter.controllers

import mu.KLogging
import org.hibernate.collection.internal.PersistentList
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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

@CrossOrigin
@RestController
@RequestMapping("/")
class TweetController(
        val tweetRepository: TweetRepository,
        val tweetLikeRepository: TweetLikeRepository,
        val responseBuilder: ResponseBuilder) {
    companion object : KLogging()

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/wall")
    fun getWall(pageable: Pageable,
                user: UserDAO): ResponseEntity<Page<TweetResponseDTO>> {
        val page = tweetRepository.findWall(
                user = user,
                pageable = pageable)

        return ResponseEntity.ok(page.map { tweet -> responseBuilder.buildTweetResponse(user, tweet) })
    }

    /**
     * Gets a paginated list of tweets by given [username]
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/tweets")
    fun getTweets(@RequestParam username: String?,
                  pageable: Pageable,
                  user: UserDAO): ResponseEntity<Page<TweetResponseDTO>> {
        if (username.isNullOrBlank()) {
            throw BadRequestException("Username must be a not empty string")
        }
        val page = tweetRepository.findAllByUserLcusernameOrderByIdDesc(username!!.toLowerCase(), pageable)

        return ResponseEntity.ok(page.map { tweet -> responseBuilder.buildTweetResponse(user, tweet) })
    }

    /**
     * Gets a page of user liked tweets
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/liked-tweets")
    fun getUserLikedTweets(@RequestParam username: String?,
                           pageable: Pageable,
                           user: UserDAO): ResponseEntity<Page<TweetResponseDTO>> {
        if (username.isNullOrBlank()) {
            throw BadRequestException("Username must be a not empty string")
        }
        val page = tweetRepository.findAllByLikes_UserLcusername(username!!.toLowerCase(), pageable)

        return ResponseEntity.ok(page.map { tweet -> responseBuilder.buildTweetResponse(user, tweet) })
    }

    /**
     * Creates a tweet and returns its new instance
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/tweets")
    fun createTweet(@RequestBody body: TweetCreateDTO,
                    user: UserDAO): ResponseEntity<TweetResponseDTO> {
        val tweetData = TweetDAO().apply {
            content = body.content
            this.user = user

            if (body.inReplyToTweetId !== null) {
                inReplyToTweet = tweetRepository.findById(body.inReplyToTweetId!!)
                        .orElseThrow { throw BadRequestException("inReplyToTweet does not exist") }
                inReplyToUser = inReplyToTweet?.user
            }
        }
        val tweet = tweetRepository.save(tweetData)

        return ResponseEntity.ok(responseBuilder.buildTweetResponse(user, tweet))
    }

    /**
     * Gets a tweet by [tweetId]
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/tweets/{tweetId}")
    fun getTweet(@PathVariable tweetId: Long,
                 user: UserDAO): ResponseEntity<TweetResponseDTO> {
        val tweet = tweetRepository.findById(tweetId).let {
            if (!it.isPresent) throw BadRequestException("Cannot find a tweet with id '$tweetId'")
            it.get()
        }

        return ResponseEntity.ok(responseBuilder.buildTweetResponse(user, tweet))
    }

    /**
     * Deletes a tweet by [tweetId] only by a tweet creator
     */
    @PreAuthorize("isAuthenticated()")
    @Transactional
    @DeleteMapping("/tweets/{tweetId}")
    fun deleteTweet(@PathVariable tweetId: Long): ResponseEntity<Unit> {
        val tweet = tweetRepository.findById(tweetId).let {
            if (!it.isPresent) throw BadRequestException("Cannot find a tweet with id '$tweetId'")
            it.get()
        }

        tweet.comments
                .forEach { t: TweetDAO? ->
                    t?.inReplyToTweet = null
                }

        tweetRepository.delete(tweet)

        return ResponseEntity.ok(Unit)
    }

    /**
     * Adds a like to a tweet.
     * Likes from user are unique per tweet
     */
    @PreAuthorize("isAuthenticated()")
    @Transactional
    @PostMapping("/tweets/{tweetId}/like")
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

        tweetLikeRepository.save(tweetLike)

        return ResponseEntity.ok(responseBuilder.buildTweetResponse(user, tweet))
    }

    /**
     * Removes a like from a tweet
     */
    @PreAuthorize("isAuthenticated()")
    @Transactional
    @PostMapping("/tweets/{tweetId}/unlike")
    fun unlikeTweet(@PathVariable tweetId: Long,
                    user: UserDAO): ResponseEntity<TweetResponseDTO> {
        val tweet = tweetRepository.findById(tweetId).let {
            if (!it.isPresent) throw BadRequestException("Cannot find a tweet with id '$tweetId'")
            it.get()
        }

        val tweetLike = tweet.likes.find {
            it?.user?.id === user.id
        }.apply {
            if (this === null) throw BadRequestException("Tweet was not liked")
        }

        (tweet.likes as PersistentList).remove(tweetLike)

        return ResponseEntity.ok(responseBuilder.buildTweetResponse(user, tweet))
    }

    /**
     * Gets a comment list of a tweet with [tweetId]
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/tweets/{tweetId}/comments")
    fun getComments(@PathVariable tweetId: Long,
                    pageable: Pageable,
                    user: UserDAO): ResponseEntity<Page<TweetResponseDTO>> {
        val baseTweet = tweetRepository.findById(tweetId).let {
            if (!it.isPresent) {
                throw BadRequestException("Tweet does not exist")
            }
            it.get()
        }
        val page = tweetRepository.findAllByInReplyToTweet(baseTweet, pageable)

        return ResponseEntity.ok(page.map { tweet -> responseBuilder.buildTweetResponse(user, tweet) })
    }
}