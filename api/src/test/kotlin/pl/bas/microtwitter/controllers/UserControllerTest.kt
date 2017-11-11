package pl.bas.microtwitter.controllers

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
import pl.bas.microtwitter.dto.UserResponseDTO
import pl.bas.microtwitter.helpers.AuthHelper
import pl.bas.microtwitter.helpers.EndpointTest
import pl.bas.microtwitter.helpers.UserHelper
import pl.bas.microtwitter.repositories.UserRepository
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    @Autowired lateinit var http: TestRestTemplate
    @Autowired lateinit var userRepository: UserRepository

    companion object : KLogging()

    lateinit var authHeaders: HttpHeaders

    @BeforeAll
    fun setUpAll() {
        userRepository.deleteAll()
        authHeaders = AuthHelper.signupAndLogin(http)
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
    inner class getUser : EndpointTest("/users/") {
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