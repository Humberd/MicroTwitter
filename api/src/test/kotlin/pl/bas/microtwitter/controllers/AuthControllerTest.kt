package pl.bas.microtwitter.controllers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import pl.bas.microtwitter.dto.SignupDTO
import pl.bas.microtwitter.helpers.EndpointTest

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AuthControllerTest {
    @Autowired lateinit var http: TestRestTemplate

    @Nested
    inner class signup : EndpointTest("/auth/signup") {
        @Test
        fun `should sign up a new user`() {
            val data = SignupDTO(
                    username = "JanKowalski",
                    email = "jan@kowalski.com",
                    fullName = "Jan Kowalski",
                    password = "admin123"
            )

            val response = http.postForEntity(url, data, String::class.java)

            assertNotNull(response)
            assertEquals(response.statusCode, HttpStatus.OK)
            assertFalse(response.body!!.equals(""))
        }

        @Test
        fun `should not signup when there is no user with the same username`() {

        }

        @Test
        fun `should not signup when there is no user with the same email`() {
            TODO("not implemented")
        }


    }

    @Nested
    inner class login : EndpointTest("/auth/login") {
        @Test
        fun `should`() {

        }

    }

    @Nested
    inner class updatePassword : EndpointTest("/auth/password") {
    }

}
