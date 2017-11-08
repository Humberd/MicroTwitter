package pl.bas.microtwitter.helpers

import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import pl.bas.microtwitter.dto.TweetResponseDTO
import pl.bas.microtwitter.dto.TweetCreateDTO

object TweetHelper {
    fun createTweet(http: TestRestTemplate): TweetResponseDTO {
        val authHeaders = AuthHelper.signupAndLogin(http)

        val tweetData = TweetCreateDTO(content = "foobar")

        http.exchange("/tweets/", HttpMethod.POST, HttpEntity(tweetData, authHeaders), TweetResponseDTO::class.java).let {
            assertEquals(it.statusCode, HttpStatus.OK)

            return it.body!!
        }
    }
}