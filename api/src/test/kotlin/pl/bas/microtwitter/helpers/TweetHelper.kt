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
            assertEquals(HttpStatus.OK, it.statusCode, it.body?.toString())

            return it.body!!
        }
    }

    fun likeTweet(tweetId: Long, http: TestRestTemplate): TweetResponseDTO {
        val authHeaders = AuthHelper.signupAndLogin(http)

        http.exchange("/tweets/${tweetId}/likes", HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).let {
            assertEquals(HttpStatus.OK, it.statusCode, it.body?.toString())

            return it.body!!
        }
    }
}