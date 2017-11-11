package pl.bas.microtwitter.helpers

import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import pl.bas.microtwitter.dto.TweetCreateDTO
import pl.bas.microtwitter.dto.TweetResponseDTO

object TweetHelper {
    fun createTweet(http: TestRestTemplate,
                    tweetData: TweetCreateDTO = TweetCreateDTO(content = "foobar")): TweetResponseDTO {
        val authHeaders = AuthHelper.signupAndLogin(http)

        http.exchange("/tweets/", HttpMethod.POST, HttpEntity(tweetData, authHeaders), TweetResponseDTO::class.java).let {
            assertEquals(HttpStatus.OK, it.statusCode, it.body?.toString())

            return it.body!!
        }
    }

    fun getTweet(http: TestRestTemplate,
                 tweetId: Long): TweetResponseDTO {
        val authHeaders = AuthHelper.signupAndLogin(http)
        http.exchange("/tweets/$tweetId", HttpMethod.GET, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).let {
            assertEquals(HttpStatus.OK, it.statusCode, it.body?.toString())

            return it.body!!
        }
    }

    fun likeTweet(http: TestRestTemplate,
                  tweetId: Long): TweetResponseDTO {
        val authHeaders = AuthHelper.signupAndLogin(http)

        http.exchange("/tweets/${tweetId}/likes", HttpMethod.POST, HttpEntity(null, authHeaders), TweetResponseDTO::class.java).let {
            assertEquals(HttpStatus.OK, it.statusCode, it.body?.toString())

            return it.body!!
        }
    }
}