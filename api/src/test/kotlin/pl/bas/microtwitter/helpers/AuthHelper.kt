package pl.bas.microtwitter.helpers

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import pl.bas.microtwitter.dto.SignupDTO

object AuthHelper {
    var user1: SignupDTO = SignupDTO("", "", "", "")
        get() = SignupDTO(
                username = "JanKowalski",
                email = "jan@kowalski.com",
                fullName = "Jan Kowalski",
                password = "admin123"
        )

    var user2: SignupDTO = SignupDTO("", "", "", "")
        get() = SignupDTO(
                username = "AdamNowak",
                email = "adam@nowak.com",
                fullName = "AdamNowak",
                password = "admin1234"
        )

    var user3: SignupDTO = SignupDTO("", "", "", "")
        get() = SignupDTO(
                username = "StefanBatory",
                email = "stefan@Batory.pl",
                fullName = "Stefan Batory",
                password = "password"
        )

    fun signUp(http: TestRestTemplate,
               user: SignupDTO = user1): ResponseEntity<String> {
        return http.postForEntity("/auth/signup", user, String::class.java)
    }

    fun login(http: TestRestTemplate,
              user: SignupDTO = user1): ResponseEntity<String> {
        return http.postForEntity("/auth/login", user, String::class.java)
    }

    fun signupAndLogin(http: TestRestTemplate,
                       user: SignupDTO = user1): HttpHeaders {
        signUp(http, user)
        val loginResponse = login(http, user)

        return HttpHeaders().apply {
            add("Authorization", loginResponse.headers.get("Authorization")?.get(0))
        }
    }

}