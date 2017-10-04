package pl.bas.microtwitter.controllers

import org.junit.jupiter.api.Assertions.*
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
    @Autowired lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun getStatus() {
        val response = testRestTemplate.getForEntity("/app/status", String::class.java)

        assertNotNull(response)
        assertEquals(response.statusCode, HttpStatus.OK)
        assertTrue(response.body?.length!! > 0)
    }

    @Test
    fun ping() {
        val response = testRestTemplate.getForEntity("/app/ping", String::class.java)

        assertNotNull(response)
        assertEquals(response.statusCode, HttpStatus.OK)
        assertEquals(response.body, "pong")
    }

}