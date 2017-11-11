package pl.bas.microtwitter.controllers

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import mu.KLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import pl.bas.microtwitter.dto.TweetResponseDTO
import pl.bas.microtwitter.dto.UserResponseDTO
import pl.bas.microtwitter.helpers.*
import pl.bas.microtwitter.repositories.UserRepository
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    @Autowired lateinit var http: TestRestTemplate
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var gson: Gson

    companion object : KLogging()

    @BeforeAll
    fun setUpAll() {
        userRepository.deleteAll()
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class getMe : EndpointTest("/me") {
        @Test
        fun `should get signed in user info`() {
            UserHelper.getMe(http).apply {
                assert(this.id is Long)
                assert(this.createdAt is Date)
                assertEquals(AuthHelper.user1.email, this.email)
                assertEquals(AuthHelper.user1.username, this.username)
                assertEquals(AuthHelper.user1.fullName, this.fullName)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class getUsers : EndpointTest("/users/") {
        val user1 = AuthHelper.user1.apply { username = "Adam"; email = "other@email.com" }
        val user2 = AuthHelper.user2.apply { username = "adamNowakowskii322" }
        val user3 = AuthHelper.user3.apply { username = "AdammBielawieckii" }

        lateinit var authHeaders: HttpHeaders

        @BeforeAll
        fun setUpAll() {
            authHeaders = AuthHelper.signupAndLogin(http)
            AuthHelper.signUp(http, user1)
            AuthHelper.signUp(http, user2)
            AuthHelper.signUp(http, user3)
        }

        @Test
        fun `should get 3 users containing 'AdaM' case insensitive`() {
            http.exchange("/users/?username=AdaM", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(3, body.content.size)
            }
        }

        @Test
        fun `should paginate 3 users containing 'AdaM' case insensitive`() {
            http.exchange("/users/?username=AdaM&size=2&page=0", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(2, body.content.size)
            }

            http.exchange("/users/?username=AdaM&size=2&page=1", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(1, body.content.size)
            }

            http.exchange("/users/?username=AdaM&size=2&page=2", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(0, body.content.size)
            }
        }

        @Test
        fun `should get 2 users containing 'KII' case insensitive`() {
            http.exchange("/users/?username=KII", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(2, body.content.size)
            }
        }

        @Test
        fun `should get 1 user containing '22' case insensitive`() {
            http.exchange("/users/?username=ki", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(3, body.content.size)
            }
        }

        @Test
        fun `should not get users when not providing username`() {
            http.exchange("/users/", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }

        @Test
        fun `should not get users when providing providing empty username`() {
            http.exchange("/users/?username=", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class getUser : EndpointTest("/users/") {
        lateinit var authHeaders: HttpHeaders

        @BeforeAll
        fun setUpAll() {
            userRepository.deleteAll()
            authHeaders = AuthHelper.signupAndLogin(http)
        }

        @Test
        fun `should get other user without private fields`() {
            AuthHelper.signUp(http, AuthHelper.user2)

            UserHelper.getUser(http, AuthHelper.user2).apply {
                assert(this.id is Long)
                assert(this.createdAt === null)
                assertNotEquals(AuthHelper.user2.email, this.email)
                assertEquals(AuthHelper.user2.username, this.username)
                assertEquals(AuthHelper.user2.fullName, this.fullName)
            }
        }

        @Test
        fun `should get other user without private fields case insensitive`() {
            AuthHelper.signUp(http, AuthHelper.user2)

            UserHelper.getUser(http, AuthHelper.user2.apply { username = username.toUpperCase() }).apply {
                assert(this.id is Long)
                assert(this.createdAt === null)
                assertNotEquals(AuthHelper.user2.email, this.email)
                assertEquals(AuthHelper.user2.username, this.username)
                assertEquals(AuthHelper.user2.fullName, this.fullName)
            }
        }

        @Test
        fun `should get my user as an alternative to 'me'`() {
            UserHelper.getUser(http, AuthHelper.user1).apply {
                assert(this.id is Long)
                assert(this.createdAt is Date)
                assertEquals(AuthHelper.user1.email, this.email)
                assertEquals(AuthHelper.user1.username, this.username)
                assertEquals(AuthHelper.user1.fullName, this.fullName)
            }
        }

        @Test
        fun `should not get a not existing user`() {
            http.exchange("/users/defenitelyNotExisingUsername", HttpMethod.GET, HttpEntity(null, authHeaders), UserResponseDTO::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)

            }
        }
    }



//    @Nested
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    inner class followUser : EndpointTest("/users/") {
//        @Test
//        fun `should follow a user`() {
//            http.exchange(url)
//        }
//    }
}