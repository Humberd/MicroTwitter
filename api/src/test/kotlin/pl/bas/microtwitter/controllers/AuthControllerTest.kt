package pl.bas.microtwitter.controllers

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import pl.bas.microtwitter.helpers.AuthHelper
import pl.bas.microtwitter.helpers.EndpointTest
import pl.bas.microtwitter.repositories.UserRepository

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
internal class AuthControllerTest {
    @Autowired lateinit var http: TestRestTemplate
    @Autowired lateinit var userRepository: UserRepository

    @Nested
    inner class signup : EndpointTest("/auth/signup") {
        @AfterEach
        fun setUp() {
            userRepository.deleteAll()
            http.restTemplate.interceptors
        }

        @Test
        fun `should sign up a new user`() {
            http.postForEntity(url, AuthHelper.user1, String::class.java).apply {
                assertEquals(this.statusCode, HttpStatus.OK)
                assertFalse(this.body!!.equals(""))
            }
        }

        @Test
        fun `should not signup when there is user with the same username`() {
            http.postForEntity(url, AuthHelper.user1, String::class.java).apply {
                assertEquals(this.statusCode, HttpStatus.OK)
            }


            val userWithSameUsername = AuthHelper.user2.apply {
                this.username = AuthHelper.user1.username
            }
            http.postForEntity(url, userWithSameUsername, String::class.java).apply {
                assertEquals(this.statusCode, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        @Test
        fun `should not signup when there is user with the same email`() {
            http.postForEntity(url, AuthHelper.user1, String::class.java).apply {
                assertEquals(this.statusCode, HttpStatus.OK)
            }


            val userWithSameEmail = AuthHelper.user2.apply {
                this.email = AuthHelper.user1.email
            }
            http.postForEntity(url, userWithSameEmail, String::class.java).apply {
                assertEquals(this.statusCode, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }


    }

    @Nested
    inner class login : EndpointTest("/auth/login") {
        @Test
        fun `should login a user`() {

        }

        @Test
        fun `should not login a not existing user`() {
        }
    }

    @Nested
    inner class updatePassword : EndpointTest("/auth/password") {
    }

}
