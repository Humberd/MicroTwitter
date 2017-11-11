package pl.bas.microtwitter.controllers

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import mu.KLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
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
import pl.bas.microtwitter.helpers.AuthHelper
import pl.bas.microtwitter.helpers.CustomPageImpl
import pl.bas.microtwitter.helpers.EndpointTest
import pl.bas.microtwitter.helpers.TweetHelper
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
        fun `should create a tweet`() {
            val data1 = TweetCreateDTO(content = "foobar")
            http.exchange(url, HttpMethod.POST, HttpEntity(data1, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
                assertEquals("foobar", this.body?.content)
                assert(tweetRepository.findAll().size == 1)
            }

            val data2 = TweetCreateDTO(content = "hello")
            http.exchange(url, HttpMethod.POST, HttpEntity(data2, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
                assertEquals("hello", this.body?.content)
                assert(tweetRepository.findAll().size == 2)
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
            super.url = "/tweets/${tweet.id}/likes"
        }

        @BeforeEach
        fun setUp() {
            tweetLikeRepository.deleteAll()
        }

        @Test
        fun `should like a tweet`() {
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
                assertEquals(1, this.body?.likes)
                assert(tweetLikeRepository.findAll().size == 1)
            }
        }

        @Test
        fun `should not like a tweet more than once`() {
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
                assertEquals(1, this.body?.likes)
                assert(tweetLikeRepository.findAll().size == 1)
            }
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, this.statusCode)
                assert(tweetLikeRepository.findAll().size == 1)
            }
        }

        @Test
        fun `should not like a not existing tweet`() {
            http.exchange("/tweets/543534534/likes", HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, this.statusCode)
                assert(tweetLikeRepository.findAll().size == 0)
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
                assertEquals(3, body.content.size)
            }
        }

        @Test
        fun `should get 3 user tweets with different username casing`() {
            http.exchange("$url?username=janKOWALSKI", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(3, body.content.size)
            }
        }

        @Test
        fun `should not get tweets of not existing user`() {
            http.exchange("$url?username=defenitelyNotExistingUser", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<TweetResponseDTO>>(this.body!!)
                assertEquals(0, body.content.size)
            }
        }
    }
}