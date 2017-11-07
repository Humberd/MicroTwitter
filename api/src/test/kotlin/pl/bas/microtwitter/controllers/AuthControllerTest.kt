package pl.bas.microtwitter.controllers

import mu.KLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
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

    companion object : KLogging()

    @Nested
    inner class signup : EndpointTest("/auth/signup") {
        @BeforeEach
        fun setUp() {
            userRepository.deleteAll()
        }

        @Test
        fun `should sign up a new user`() {
            AuthHelper.signUp(http, AuthHelper.user3).apply {
                assertEquals(this.statusCode, HttpStatus.OK)
                assertFalse(this.body!!.equals(""))
            }
        }

        @Test
        fun `should not signup when there is user with the same username`() {
            AuthHelper.signUp(http, AuthHelper.user3).apply {
                assertEquals(this.statusCode, HttpStatus.OK)
            }

            val userWithSameUsername = AuthHelper.user2.apply {
                this.username = AuthHelper.user3.username
            }
            AuthHelper.signUp(http, userWithSameUsername).apply {
                assertEquals(this.statusCode, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        @Test
        fun `should not signup when there is user with the same email`() {
            AuthHelper.signUp(http, AuthHelper.user3).apply {
                assertEquals(this.statusCode, HttpStatus.OK)
            }

            val userWithSameEmail = AuthHelper.user2.apply {
                this.email = AuthHelper.user3.email
            }
            AuthHelper.signUp(http, userWithSameEmail).apply {
                assertEquals(this.statusCode, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }


    }

    @Nested
    inner class login : EndpointTest("/auth/login") {
        @BeforeEach
        internal fun setUp() {
            userRepository.deleteAll()
        }

        @Test
        fun `should login a user`() {
            AuthHelper.signUp(http)

            AuthHelper.login(http).apply {
                assertEquals(this.statusCode, HttpStatus.OK)
                assert(this.headers.get("Authorization")?.get(0)?.startsWith("Bearer")!!)
                assert(this.headers.get("Authorization")?.get(0)?.length!! > 20)
            }
        }

        @Test
        fun `should not login a not existing user`() {
            AuthHelper.login(http).apply {
                assertEquals(this.statusCode, HttpStatus.FORBIDDEN)
            }
        }
    }

    @Nested
    inner class updatePassword : EndpointTest("/auth/password") {
    }

}
