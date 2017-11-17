package pl.bas.microtwitter.controllers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AppControllerTest {
    @Autowired lateinit var http: TestRestTemplate

    @Test
    fun getStatus() {
        val response = http.getForEntity("/app/status", String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body?.length!! > 0)
    }

    @Test
    fun ping() {
        val response = http.getForEntity("/app/ping", String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("pong", response.body)
    }

}