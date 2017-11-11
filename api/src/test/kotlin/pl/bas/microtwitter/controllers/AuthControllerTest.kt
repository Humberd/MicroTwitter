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
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import pl.bas.microtwitter.dto.UpdatePasswordDTO
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
                assertEquals(HttpStatus.OK, this.statusCode)
                assertFalse(this.body!!.equals(""))
            }
        }

        @Test
        fun `should not signup when there is user with the same username`() {
            AuthHelper.signUp(http, AuthHelper.user3).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
            }

            val userWithSameUsername = AuthHelper.user2.apply {
                this.username = AuthHelper.user3.username
            }
            AuthHelper.signUp(http, userWithSameUsername).apply {
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, this.statusCode)
            }
        }

        @Test
        fun `should not signup when there is user with the same email`() {
            AuthHelper.signUp(http, AuthHelper.user3).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
            }

            val userWithSameEmail = AuthHelper.user2.apply {
                this.email = AuthHelper.user3.email
            }
            AuthHelper.signUp(http, userWithSameEmail).apply {
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, this.statusCode)
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
                assertEquals(HttpStatus.OK, this.statusCode)
                assert(this.headers.get("Authorization")?.get(0)?.startsWith("Bearer")!!)
                assert(this.headers.get("Authorization")?.get(0)?.length!! > 20)
            }
        }

        @Test
        fun `should not login a not existing user`() {
            AuthHelper.login(http).apply {
                assertEquals(HttpStatus.FORBIDDEN, this.statusCode)
            }
        }
    }

    @Nested
    inner class updatePassword : EndpointTest("/auth/password") {
        lateinit var authHeaders: HttpHeaders

        @BeforeEach
        fun setUp() {
            userRepository.deleteAll()
            authHeaders = AuthHelper.signupAndLogin(http)
        }

        @Test
        fun `should update a user password`() {
            val data = UpdatePasswordDTO(
                    oldPassword = "admin123",
                    newPassword = "admin"
            )

            http.exchange(url, HttpMethod.POST, HttpEntity(data, authHeaders), String::class.java).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
            }

            AuthHelper.login(http, AuthHelper.user1.apply { password = "admin" }).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
            }
        }

        @Test
        fun `should not update a user passwor when the old password is incorrect`() {
            val data = UpdatePasswordDTO(
                    oldPassword = "notAValidPassword",
                    newPassword = "admin"
            )

            http.exchange(url, HttpMethod.POST, HttpEntity(data, authHeaders), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, this.statusCode)
            }

            AuthHelper.login(http, AuthHelper.user1).apply {
                assertEquals(HttpStatus.OK, this.statusCode)
            }
        }
    }

}
