package pl.bas.microtwitter.controllers

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import mu.KLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import pl.bas.microtwitter.dto.TweetCreateDTO
import pl.bas.microtwitter.dto.TweetResponseDTO
import pl.bas.microtwitter.helpers.*
import pl.bas.microtwitter.repositories.TweetLikeRepository
import pl.bas.microtwitter.repositories.TweetRepository
import pl.bas.microtwitter.repositories.UserRepository

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TweetControllerTest {
    @Autowired lateinit var http: TestRestTemplate
    @Autowired lateinit var tweetRepository: TweetRepository
    @Autowired lateinit var tweetLikeRepository: TweetLikeRepository
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var gson: Gson

    companion object : KLogging()

    lateinit var authHeaders: HttpHeaders

    @BeforeAll
    fun setUpAll() {
        userRepository.deleteAll()
        authHeaders = AuthHelper.signupAndLogin(http)
    }

    @Nested
    inner class createTweet : EndpointTest("/tweets/") {
        @BeforeEach
        fun setUp() {
            tweetRepository.deleteAll()
        }

        @Test
        fun `should create 2 separate tweets`() {
            val data1 = TweetCreateDTO(content = "foobar")
            http.exchange(url, HttpMethod.POST, HttpEntity(data1, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
                assertEquals("foobar", this.body?.content)
                assertEquals(1, tweetRepository.findAll().size)
            }

            val data2 = TweetCreateDTO(content = "hello")
            http.exchange(url, HttpMethod.POST, HttpEntity(data2, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
                assertEquals("hello", this.body?.content)
                assertEquals(2, tweetRepository.findAll().size)
            }
        }

        @Test
        fun `should create a tweet in response of the other tweet`() {
            val data1 = TweetCreateDTO(content = "foobar")
            val tweet1Id = TweetHelper.createTweet(http, data1).id.apply {
                assert(this is Long)
            }

            val data2 = TweetCreateDTO(content = "hello", inReplyToTweetId = tweet1Id)
            TweetHelper.createTweet(http, data2).apply {
                assertEquals(0, this.commentsCount)
            }

            TweetHelper.getTweet(http, tweet1Id!!).apply {
                assertEquals(1, this.commentsCount)
            }
        }

        @Test
        fun `should not create a tweet in response to the not existing tweet`() {
            val notExistingTweetId: Long = 6239045803485039852
            val data2 = TweetCreateDTO(content = "hello", inReplyToTweetId = notExistingTweetId)

            http.exchange(url, HttpMethod.POST, HttpEntity(data2, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, this.statusCode)
            }
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class getTweets : EndpointTest("/tweets/") {
        @BeforeAll
        fun setUpAll() {
            tweetRepository.deleteAll()

            TweetHelper.createTweet(http)
            TweetHelper.createTweet(http)
            TweetHelper.createTweet(http)
        }

        @Test
        fun `should get 3 user tweets`() {
            http.exchange("$url?username=JanKowalski", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(3, body.content.size)
            }
        }

        @Test
        fun `should get 3 user tweets with different username casing`() {
            http.exchange("$url?username=janKOWALSKI", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(3, body.content.size)
            }
        }

        @Test
        fun `should paginate user tweets`() {
            http.exchange("$url?username=JanKowalski&size=2&page=0", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(2, body.content.size)
            }

            http.exchange("$url?username=JanKowalski&size=2&page=1", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(1, body.content.size)
            }

            http.exchange("$url?username=JanKowalski&size=2&page=2", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(0, body.content.size)
            }
        }

        @Test
        fun `should not get tweets of a not existing user`() {
            http.exchange("$url?username=defenitelyNotExistingUser", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(0, body.content.size)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class getTweet : EndpointTest("/tweets/") {
        var tweetId: Long = 0

        @BeforeAll
        fun setUpAll() {
            tweetRepository.deleteAll()
            tweetId = TweetHelper.createTweet(http).id!!
        }

        @Test
        fun `should get a tweet by id`() {
            http.exchange("$url/$tweetId", HttpMethod.GET, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(tweetId, body?.id)
            }
        }

        @Test
        fun `should not get a tweet by not existing id`() {
            http.exchange("$url/defenitelyNotAValidTweetId", HttpMethod.GET, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class deleteTweet : EndpointTest("/tweets/") {
        var tweetId: Long = 0

        @BeforeEach
        fun setUp() {
            tweetRepository.deleteAll()
            tweetLikeRepository.deleteAll()

            tweetId = TweetHelper.createTweet(http).id!!

            TweetHelper.likeTweet(http, tweetId)
        }

        @Test
        fun `should delete a tweet and all of its likes by id `() {
            assertEquals(1, tweetRepository.findAll().size)
            assertEquals(1, tweetLikeRepository.findAll().size)

            http.exchange("$url/$tweetId", HttpMethod.DELETE, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, statusCode)
            }

            assertEquals(0, tweetRepository.findAll().size)
            assertEquals(0, tweetLikeRepository.findAll().size)
        }

        @Test
        fun `should not delete w tweet by a not existing id`() {
            http.exchange("$url/defenitelyNotExistingTwitterId", HttpMethod.DELETE, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }

            assertEquals(1, tweetRepository.findAll().size)
            assertEquals(1, tweetLikeRepository.findAll().size)
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class unlikeTweet : EndpointTest("") {
        lateinit var tweet: TweetResponseDTO

        @BeforeAll
        fun setUpAll() {
            tweetRepository.deleteAll()

            tweet = TweetHelper.createTweet(http)
            super.url = "/tweets/${tweet.id}/unlike"
        }

        @BeforeEach
        fun setUp() {
            tweetLikeRepository.deleteAll()
        }

        @Test
        fun `should unlike a tweet`() {
            UserHelper.getMe(http).apply {
                assertEquals(0, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertFalse(this.isLiked!!)
            }
            TweetHelper.likeTweet(http, tweet.id!!)
            UserHelper.getMe(http).apply {
                assertEquals(1, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertTrue(this.isLiked!!)
            }
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
                assertEquals(0, this.body?.likesCount)
                assertEquals(0, tweetLikeRepository.findAll().size)
            }
            UserHelper.getMe(http).apply {
                assertEquals(0, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertFalse(this.isLiked!!)
            }
        }

        @Test
        fun `should not unlike a tweet when the tweet was not liked`() {
            UserHelper.getMe(http).apply {
                assertEquals(0, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertFalse(this.isLiked!!)
            }
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, this.statusCode)
            }
            UserHelper.getMe(http).apply {
                assertEquals(0, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertFalse(this.isLiked!!)
            }
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class likeTweet : EndpointTest("") {
        lateinit var tweet: TweetResponseDTO

        @BeforeAll
        fun setUpAll() {
            tweetRepository.deleteAll()

            tweet = TweetHelper.createTweet(http)
            super.url = "/tweets/${tweet.id}/like"
        }

        @BeforeEach
        fun setUp() {
            tweetLikeRepository.deleteAll()
        }

        @Test
        fun `should like a tweet`() {
            UserHelper.getMe(http).apply {
                assertEquals(0, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertFalse(this.isLiked!!)
            }
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
                assertEquals(1, this.body?.likesCount)
                assertEquals(1, tweetLikeRepository.findAll().size)
            }
            UserHelper.getMe(http).apply {
                assertEquals(1, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertTrue(this.isLiked!!)
            }
            TweetHelper.getTweet(http, tweet.id!!, AuthHelper.user3).apply {
                assertFalse(this.isLiked!!)
            }
        }

        @Test
        fun `should not like a tweet more than once`() {
            UserHelper.getMe(http).apply {
                assertEquals(0, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertFalse(this.isLiked!!)
            }
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
                assertEquals(1, this.body?.likesCount)
                assertEquals(1, tweetLikeRepository.findAll().size)
            }
            UserHelper.getMe(http).apply {
                assertEquals(1, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertTrue(this.isLiked!!)
            }
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, this.statusCode)
                assertEquals(1, tweetLikeRepository.findAll().size)
            }
            UserHelper.getMe(http).apply {
                assertEquals(1, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertTrue(this.isLiked!!)
            }
        }

        @Test
        fun `should not like a not existing tweet`() {
            UserHelper.getMe(http).apply {
                assertEquals(0, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertFalse(this.isLiked!!)
            }
            http.exchange("/tweets/543534534/like", HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, this.statusCode)
                assertEquals(0, tweetLikeRepository.findAll().size)
            }
            UserHelper.getMe(http).apply {
                assertEquals(0, likesCount)
            }
            TweetHelper.getTweet(http, tweet.id!!).apply {
                assertFalse(this.isLiked!!)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class getComments : EndpointTest("/tweets/") {
        @BeforeAll
        fun setUpAll() {
            tweetRepository.deleteAll()

            val tweet = TweetHelper.createTweet(http)
            TweetHelper.createTweet(http, TweetCreateDTO(content = "a", inReplyToTweetId = tweet.id))
            TweetHelper.createTweet(http, TweetCreateDTO(content = "b", inReplyToTweetId = tweet.id))
            TweetHelper.createTweet(http, TweetCreateDTO(content = "c", inReplyToTweetId = tweet.id))

            super.url = "/tweets/${tweet.id}/comments"
        }

        @Test
        fun `should get 3 comments for a tweet`() {
            http.exchange(url , HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(3, body.content.size)
            }
        }

        @Test
        fun `should paginate comments`() {
            http.exchange("$url?size=2&page=0" , HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(2, body.content.size)
            }

            http.exchange("$url?size=2&page=1" , HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(1, body.content.size)
            }

            http.exchange("$url?size=2&page=2" , HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(0, body.content.size)
            }
        }

        @Test
        fun `should not get comments of a not existing tweet`() {
            http.exchange("/tweets/243983904203984/comments" , HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }
    }
}