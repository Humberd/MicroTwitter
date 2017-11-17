package pl.bas.microtwitter.helpers

import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import pl.bas.microtwitter.dto.SignupDTO
import pl.bas.microtwitter.dto.UserResponseDTO

object UserHelper {
    fun getMe(http: TestRestTemplate,
              authUser: SignupDTO = AuthHelper.user1): UserResponseDTO {
        val authHeaders = AuthHelper.signupAndLogin(http, authUser)

        http.exchange("/me", HttpMethod.GET, HttpEntity(null, authHeaders), UserResponseDTO::class.java).let {
            assertEquals(HttpStatus.OK, it.statusCode, it.body?.toString())

            return it.body!!
        }
    }

    fun getUser(http: TestRestTemplate,
                user: SignupDTO,
                authUser: SignupDTO = AuthHelper.user1): UserResponseDTO {
        val authHeaders = AuthHelper.signupAndLogin(http, authUser)

        http.exchange("/users/${user.username}", HttpMethod.GET, HttpEntity(null, authHeaders), UserResponseDTO::class.java).let {
            assertEquals(HttpStatus.OK, it.statusCode, it.body?.toString())

            return it.body!!
        }
    }

    fun followUser(http: TestRestTemplate,
                   user: UserResponseDTO,
                   authUser:SignupDTO = AuthHelper.user1): UserResponseDTO {
        val authHeaders = AuthHelper.signupAndLogin(http, authUser)

        http.exchange("/users/${user.id}/follow", HttpMethod.POST, HttpEntity(null, authHeaders), UserResponseDTO::class.java).let {
            assertEquals(HttpStatus.OK, it.statusCode, it.body?.toString())

            return it.body!!
        }
    }
}