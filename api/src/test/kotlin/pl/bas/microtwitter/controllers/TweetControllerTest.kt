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
import pl.bas.microtwitter.dto.UserResponseDTO
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
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class getWall : EndpointTest("/wall") {
        lateinit var user1: UserResponseDTO
        lateinit var user2: UserResponseDTO
        lateinit var user3: UserResponseDTO
        lateinit var user4: UserResponseDTO


        @BeforeEach
        fun setUp() {
            userRepository.deleteAll()
            //user 1
            AuthHelper.signUp(http, AuthHelper.user1)
            user1 = UserHelper.getMe(http, AuthHelper.user1)
            //user 2
            AuthHelper.signUp(http, AuthHelper.user2)
            user2 = UserHelper.getMe(http, AuthHelper.user2)
            //user 3
            AuthHelper.signUp(http, AuthHelper.user3)
            user3 = UserHelper.getMe(http, AuthHelper.user3)
            //user 4
            AuthHelper.signUp(http, AuthHelper.user4)
            user4 = UserHelper.getMe(http, AuthHelper.user4)


            TweetHelper.createTweet(http, TweetCreateDTO(content = "newcontent"), AuthHelper.user1)
            TweetHelper.createTweet(http, TweetCreateDTO(content = "foobar1"), AuthHelper.user2)
            TweetHelper.createTweet(http, TweetCreateDTO(content = "foobar2"), AuthHelper.user2)
            TweetHelper.createTweet(http, TweetCreateDTO(content = "foobar3"), AuthHelper.user2)
            TweetHelper.createTweet(http, TweetCreateDTO(content = "not a game"), AuthHelper.user3)
            TweetHelper.createTweet(http, TweetCreateDTO(content = "new game is comming"), AuthHelper.user4)
        }

        @Test
        fun `should get user wall | user1 follows user2,user4 = 5 tweets`() {
            UserHelper.followUser(http, user2, AuthHelper.user1)
            UserHelper.followUser(http, user4, AuthHelper.user1)

            val headers = AuthHelper.signupAndLogin(http, AuthHelper.user1)
            http.exchange(url, HttpMethod.GET, HttpEntity(null, headers), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(5, body.content.size)
            }
        }

        @Test
        fun `should get user wall | user3 follows user1,user4 = 3 tweets`() {
            UserHelper.followUser(http, user1, AuthHelper.user3)
            UserHelper.followUser(http, user4, AuthHelper.user3)

            val headers = AuthHelper.signupAndLogin(http, AuthHelper.user3)
            http.exchange(url, HttpMethod.GET, HttpEntity(null, headers), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(3, body.content.size)
            }
        }
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
                assertEquals(AuthHelper.user1.username, this.body?.user?.username)
                assertEquals(AuthHelper.user1.fullName, this.body?.user?.fullName)
                assertEquals(null, this.body?.inReplyToTweetId)
                assertEquals(null, this.body?.inReplyToUser?.id)
                assertEquals(null, this.body?.inReplyToUser?.username)
                assertEquals(null, this.body?.inReplyToUser?.fullName)
                assertEquals(1, tweetRepository.findAll().size)
            }

            val data2 = TweetCreateDTO(content = "hello")
            http.exchange(url, HttpMethod.POST, HttpEntity(data2, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
                assertEquals("hello", this.body?.content)
                assertEquals(AuthHelper.user1.username, this.body?.user?.username)
                assertEquals(AuthHelper.user1.fullName, this.body?.user?.fullName)
                assertEquals(null, this.body?.inReplyToTweetId)
                assertEquals(null, this.body?.inReplyToUser?.id)
                assertEquals(null, this.body?.inReplyToUser?.username)
                assertEquals(null, this.body?.inReplyToUser?.fullName)
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
                assertEquals(AuthHelper.user1.username, this.user?.username)
                assertEquals(AuthHelper.user1.fullName, this.user?.fullName)
                assertEquals(tweet1Id, this.inReplyToTweetId)
                assert(this.inReplyToUser?.id is Long)
                assertEquals(AuthHelper.user1.username, this.inReplyToUser?.username)
                assertEquals(AuthHelper.user1.fullName, this.inReplyToUser?.fullName)
            }

            TweetHelper.getTweet(http, tweet1Id!!).apply {
                assertEquals(1, this.commentsCount)
                assertEquals(AuthHelper.user1.username, this.user?.username)
                assertEquals(AuthHelper.user1.fullName, this.user?.fullName)
                assertEquals(null, this.inReplyToTweetId)
                assertEquals(null, this.inReplyToUser?.id)
                assertEquals(null, this.inReplyToUser?.username)
                assertEquals(null, this.inReplyToUser?.fullName)
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

        @Test
        fun `should not get tweets when the user name is not provided`() {
            http.exchange(url, HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetUserLikedTweets: EndpointTest("/liked-tweets") {
        lateinit var authHeaders1: HttpHeaders
        lateinit var authHeaders2: HttpHeaders
        lateinit var user1LikedTweetsUrl: String
        lateinit var user2LikedTweetsUrl: String

        @BeforeAll
        fun setUp() {
            userRepository.deleteAll()
            tweetLikeRepository.deleteAll()
            tweetRepository.deleteAll()

            authHeaders1 = AuthHelper.signupAndLogin(http, AuthHelper.user1)
            UserHelper.getMe(http, AuthHelper.user1).apply {
                user1LikedTweetsUrl = "/liked-tweets?username=$username"
            }
            authHeaders2 = AuthHelper.signupAndLogin(http, AuthHelper.user2)
            UserHelper.getMe(http, AuthHelper.user2).apply {
                user2LikedTweetsUrl = "/liked-tweets?username=$username"
            }

            val tweet1 = TweetHelper.createTweet(http, TweetCreateDTO("its ok"), AuthHelper.user1)
            val tweet2 = TweetHelper.createTweet(http, TweetCreateDTO("foobar 2"), AuthHelper.user1)
            val tweet3 = TweetHelper.createTweet(http, TweetCreateDTO("foobar 3"), AuthHelper.user1)

            TweetHelper.likeTweet(http, tweet1.id!!, AuthHelper.user1)
            TweetHelper.likeTweet(http, tweet1.id!!, AuthHelper.user2)
            TweetHelper.likeTweet(http, tweet2.id!!, AuthHelper.user2)
            TweetHelper.likeTweet(http, tweet3.id!!, AuthHelper.user2)
        }

        @Test
        fun `should get list of 1 tweets liked by user 1`() {
            http.exchange(user1LikedTweetsUrl, HttpMethod.GET, HttpEntity(null, authHeaders1), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                println(this.body)
                assertEquals(1, body.content.size)
            }
        }

        @Test
        fun `should get list of 3 tweets liked by user 2`() {
            http.exchange(user2LikedTweetsUrl, HttpMethod.GET, HttpEntity(null, authHeaders2), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(3, body.content.size)
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
            http.exchange(url, HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(3, body.content.size)
            }
        }

        @Test
        fun `should paginate comments`() {
            http.exchange("$url?size=2&page=0", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(2, body.content.size)
            }

            http.exchange("$url?size=2&page=1", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(1, body.content.size)
            }

            http.exchange("$url?size=2&page=2", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(0, body.content.size)
            }
        }

        @Test
        fun `should not get comments of a not existing tweet`() {
            http.exchange("/tweets/243983904203984/comments", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }
    }
}