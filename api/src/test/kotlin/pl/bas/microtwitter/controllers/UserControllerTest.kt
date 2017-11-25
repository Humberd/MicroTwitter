package pl.bas.microtwitter.controllers

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import mu.KLogging
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import pl.bas.microtwitter.dto.BirthdateDTO
import pl.bas.microtwitter.dto.ProfileUpdateDTO
import pl.bas.microtwitter.dto.UserResponseDTO
import pl.bas.microtwitter.helpers.AuthHelper
import pl.bas.microtwitter.helpers.CustomPageImpl
import pl.bas.microtwitter.helpers.EndpointTest
import pl.bas.microtwitter.helpers.UserHelper
import pl.bas.microtwitter.repositories.UserRepository
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    @Autowired lateinit var http: TestRestTemplate
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var gson: Gson

    companion object : KLogging()

    @BeforeAll
    fun setUpAll() {
        userRepository.deleteAll()
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class getMe : EndpointTest("/me") {
        @Test
        fun `should get signed in user info`() {
            UserHelper.getMe(http).apply {
                assert(this.id is Long)
                assert(this.createdAt is Date)
                assertEquals(AuthHelper.user1.email, this.email)
                assertEquals(AuthHelper.user1.username, this.username)
                assertEquals(AuthHelper.user1.fullName, this.profile?.fullName)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class updateProfile : EndpointTest("/me/profile") {
        lateinit var authHeaders: HttpHeaders
        val newProfileData = ProfileUpdateDTO(
                fullName = "Pawel Gawowski",
                description = "foobar",
                location = "location",
                profileLinkColor = "#aa14a1",
                url = "www.google.com",
                birthdate = BirthdateDTO(
                        day = 1,
                        month = 1,
                        year = 1978
                )
        )

        @BeforeEach
        fun setUp() {
            authHeaders = AuthHelper.signupAndLogin(http)
        }

        @Test
        fun `should update profile`() {
            http.exchange(url, HttpMethod.PUT, HttpEntity(newProfileData, authHeaders), UserResponseDTO::class.java).apply {
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(newProfileData.fullName, body?.profile?.fullName)
                assertEquals(newProfileData.description, body?.profile?.description)
                assertEquals(newProfileData.location, body?.profile?.location)
                assertEquals(newProfileData.profileLinkColor, body?.profile?.profileLinkColor)
                assertEquals(newProfileData.url, body?.profile?.url)
                assertEquals(newProfileData.birthdate?.day, body?.profile?.birthdate?.day)
                assertEquals(newProfileData.birthdate?.month, body?.profile?.birthdate?.month)
                assertEquals(newProfileData.birthdate?.year, body?.profile?.birthdate?.year)
            }
        }

        @Test
        fun `should not update profile when the day is invalid`() {
            val badProfileData = newProfileData.copy().apply {
                birthdate = birthdate?.copy()!!.apply {
                    day = 0
                }
            }
            http.exchange(url, HttpMethod.PUT, HttpEntity(badProfileData, authHeaders), UserResponseDTO::class.java).apply {
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode)
            }
        }

        @Test
        fun `should not update profile when the hex color has invalid format`() {
            val badProfileData = newProfileData.copy().apply {
                profileLinkColor = "not a valid color"
            }
            http.exchange(url, HttpMethod.PUT, HttpEntity(badProfileData, authHeaders), UserResponseDTO::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class getUsers : EndpointTest("/users/") {
        val user1 = AuthHelper.user1.apply { username = "Adam"; email = "other@email.com"; fullName = "Piotr Wielki" }
        val user2 = AuthHelper.user2.apply { username = "adamMostowiak22"; fullName = "Anna Wielka" }
        val user3 = AuthHelper.user3.apply { username = "AdammBielawieckii"; fullName = "Jan Chlewiak" }

        lateinit var authHeaders: HttpHeaders

        @BeforeAll
        fun setUpAll() {
            userRepository.deleteAll()
            authHeaders = AuthHelper.signupAndLogin(http)
            AuthHelper.signUp(http, user1)
            AuthHelper.signUp(http, user2)
            AuthHelper.signUp(http, user3)
        }

        @Test
        fun `should get 2 users containing 'WieLk' fullName case insensitive`() {
            http.exchange("/users/?usernameOrfullName=WieLk", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(2, body.content.size)
            }
        }

        @Test
        fun `should get 2 users containing 'WIAK' username and fullName case insensitive`() {
            http.exchange("/users/?usernameOrfullName=WIAK", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(2, body.content.size)
            }
        }

        @Test
        fun `should get 3 users containing 'AdaM' username case insensitive`() {
            http.exchange("/users/?usernameOrfullName=AdaM", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(3, body.content.size)
            }
        }

        @Test
        fun `should paginate 3 users containing 'AdaM' username case insensitive`() {
            http.exchange("/users/?usernameOrfullName=AdaM&size=2&page=0", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(2, body.content.size)
            }

            http.exchange("/users/?usernameOrfullName=AdaM&size=2&page=1", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(1, body.content.size)
            }

            http.exchange("/users/?usernameOrfullName=AdaM&size=2&page=2", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(0, body.content.size)
            }
        }

        @Test
        fun `should get 1 user containing 'KII' username case insensitive`() {
            http.exchange("/users/?usernameOrfullName=KII", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(1, body.content.size)
            }
        }

        @Test
        fun `should get 1 user containing '22' username case insensitive`() {
            http.exchange("/users/?usernameOrfullName=22", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                val body = gson.fromJson<CustomPageImpl<UserResponseDTO>>(this.body!!)
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(1, body.content.size)
            }
        }

        @Test
        fun `should not get users when not providing username`() {
            http.exchange("/users/", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }

        @Test
        fun `should not get users when providing providing empty username`() {
            http.exchange("/users/?usernameOrfullName=", HttpMethod.GET, HttpEntity(null, authHeaders), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class getUser : EndpointTest("/users/") {
        lateinit var authHeaders: HttpHeaders

        @BeforeAll
        fun setUpAll() {
            userRepository.deleteAll()
            authHeaders = AuthHelper.signupAndLogin(http)
        }

        @Test
        fun `should get other user without private fields`() {
            AuthHelper.signUp(http, AuthHelper.user2)

            UserHelper.getUser(http, AuthHelper.user2).apply {
                assert(this.id is Long)
                assert(this.createdAt === null)
                assertNotEquals(AuthHelper.user2.email, this.email)
                assertEquals(AuthHelper.user2.username, this.username)
                assertEquals(AuthHelper.user2.fullName, this.profile?.fullName)
            }
        }

        @Test
        fun `should get other user without private fields case insensitive`() {
            AuthHelper.signUp(http, AuthHelper.user2)

            UserHelper.getUser(http, AuthHelper.user2.apply { username = username.toUpperCase() }).apply {
                assert(this.id is Long)
                assert(this.createdAt === null)
                assertNotEquals(AuthHelper.user2.email, this.email)
                assertEquals(AuthHelper.user2.username, this.username)
                assertEquals(AuthHelper.user2.fullName, this.profile?.fullName)
            }
        }

        @Test
        fun `should get my user as an alternative to 'me'`() {
            UserHelper.getUser(http, AuthHelper.user1).apply {
                assert(this.id is Long)
                assert(this.createdAt is Date)
                assertEquals(AuthHelper.user1.email, this.email)
                assertEquals(AuthHelper.user1.username, this.username)
                assertEquals(AuthHelper.user1.fullName, this.profile?.fullName)
            }
        }

        @Test
        fun `should not get a not existing user`() {
            http.exchange("/users/defenitelyNotExisingUsername", HttpMethod.GET, HttpEntity(null, authHeaders), UserResponseDTO::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)

            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class followUser : EndpointTest("/users/") {
        lateinit var authHeaders1: HttpHeaders
        lateinit var authHeaders2: HttpHeaders

        @BeforeEach
        fun setUp() {
            userRepository.deleteAll()
            authHeaders1 = AuthHelper.signupAndLogin(http, AuthHelper.user1)
            UserHelper.getMe(http, AuthHelper.user1).apply {
                url = "/users/$id/follow"
            }
            authHeaders2 = AuthHelper.signupAndLogin(http, AuthHelper.user2)
        }

        @Test
        fun `should follow a user`() {
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders2), String::class.java).apply {
                assertEquals(HttpStatus.OK, statusCode)
            }
            UserHelper.getMe(http, AuthHelper.user1).apply {
                assertEquals(0, followedUsersCount)
                assertEquals(1, followedByUsersCount)
            }
            UserHelper.getMe(http, AuthHelper.user2).apply {
                assertEquals(1, followedUsersCount)
                assertEquals(0, followedByUsersCount)
            }
            UserHelper.getUser(http, user = AuthHelper.user1, authUser = AuthHelper.user2).apply {
                assertTrue(this.isFollowing!!)
            }
        }

        @Test
        fun `should not follow a user when the user is already followed`() {
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders2), String::class.java).apply {
                assertEquals(HttpStatus.OK, statusCode)
            }
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders2), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
            UserHelper.getMe(http, AuthHelper.user1).apply {
                assertEquals(0, followedUsersCount)
                assertEquals(1, followedByUsersCount)
            }
            UserHelper.getMe(http, AuthHelper.user2).apply {
                assertEquals(1, followedUsersCount)
                assertEquals(0, followedByUsersCount)
            }
            UserHelper.getUser(http, user = AuthHelper.user1, authUser = AuthHelper.user2).apply {
                assertTrue(this.isFollowing!!)
            }
        }

        @Test
        fun `should not follow myself`() {
            http.exchange(url, HttpMethod.POST, HttpEntity(null, authHeaders1), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }

        @Test
        fun `should not follow a not existing user`() {
            http.exchange("/users/defenitelyNotExistingUserId/follow", HttpMethod.POST, HttpEntity(null, authHeaders1), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class unfollowUser : EndpointTest("/users/") {
        lateinit var authHeaders1: HttpHeaders
        lateinit var authHeaders2: HttpHeaders
        lateinit var followUrl: String
        lateinit var unfollowUrl: String

        @BeforeEach
        fun setUp() {
            userRepository.deleteAll()
            authHeaders1 = AuthHelper.signupAndLogin(http, AuthHelper.user1)
            UserHelper.getMe(http, AuthHelper.user1).apply {
                followUrl = "/users/$id/follow"
                unfollowUrl = "/users/$id/unfollow"
            }
            authHeaders2 = AuthHelper.signupAndLogin(http, AuthHelper.user2)
        }

        @Test
        fun `should unfollow a user`() {
            http.exchange(followUrl, HttpMethod.POST, HttpEntity(null, authHeaders2), String::class.java).apply {
                assertEquals(HttpStatus.OK, statusCode)
            }
            UserHelper.getMe(http, AuthHelper.user1).apply {
                assertEquals(0, followedUsersCount)
                assertEquals(1, followedByUsersCount)
            }
            UserHelper.getMe(http, AuthHelper.user2).apply {
                assertEquals(1, followedUsersCount)
                assertEquals(0, followedByUsersCount)
            }
            UserHelper.getUser(http, user = AuthHelper.user1, authUser = AuthHelper.user2).apply {
                assertTrue(this.isFollowing!!)
            }

            http.exchange(unfollowUrl, HttpMethod.POST, HttpEntity(null, authHeaders2), String::class.java).apply {
                assertEquals(HttpStatus.OK, statusCode)
            }
            UserHelper.getMe(http, AuthHelper.user1).apply {
                assertEquals(0, followedUsersCount)
                assertEquals(0, followedByUsersCount)
            }
            UserHelper.getMe(http, AuthHelper.user2).apply {
                assertEquals(0, followedUsersCount)
                assertEquals(0, followedByUsersCount)
            }
            UserHelper.getUser(http, user = AuthHelper.user1, authUser = AuthHelper.user2).apply {
                assertFalse(this.isFollowing!!)
            }
        }

        @Test
        fun `should not unfollow a user when the user was not already followed`() {
            http.exchange(followUrl, HttpMethod.POST, HttpEntity(null, authHeaders2), String::class.java).apply {
                assertEquals(HttpStatus.OK, statusCode)
            }
            UserHelper.getMe(http, AuthHelper.user1).apply {
                assertEquals(0, followedUsersCount)
                assertEquals(1, followedByUsersCount)
            }
            UserHelper.getMe(http, AuthHelper.user2).apply {
                assertEquals(1, followedUsersCount)
                assertEquals(0, followedByUsersCount)
            }
            UserHelper.getUser(http, user = AuthHelper.user1, authUser = AuthHelper.user2).apply {
                assertTrue(this.isFollowing!!)
            }

            http.exchange(unfollowUrl, HttpMethod.POST, HttpEntity(null, authHeaders2), String::class.java).apply {
                assertEquals(HttpStatus.OK, statusCode)
            }
            UserHelper.getMe(http, AuthHelper.user1).apply {
                assertEquals(0, followedUsersCount)
                assertEquals(0, followedByUsersCount)
            }
            UserHelper.getMe(http, AuthHelper.user2).apply {
                assertEquals(0, followedUsersCount)
                assertEquals(0, followedByUsersCount)
            }
            UserHelper.getUser(http, user = AuthHelper.user1, authUser = AuthHelper.user2).apply {
                assertFalse(this.isFollowing!!)
            }

            http.exchange(unfollowUrl, HttpMethod.POST, HttpEntity(null, authHeaders2), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
            UserHelper.getMe(http, AuthHelper.user1).apply {
                assertEquals(0, followedUsersCount)
                assertEquals(0, followedByUsersCount)
            }
            UserHelper.getMe(http, AuthHelper.user2).apply {
                assertEquals(0, followedUsersCount)
                assertEquals(0, followedByUsersCount)
            }
            UserHelper.getUser(http, user = AuthHelper.user1, authUser = AuthHelper.user2).apply {
                assertFalse(this.isFollowing!!)
            }
        }

        @Test
        fun `should not unfollow myself`() {
            http.exchange(unfollowUrl, HttpMethod.POST, HttpEntity(null, authHeaders1), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }

        @Test
        fun `should not unfollow a not existing user`() {
            http.exchange("/users/defenitelyNotExistingUserId/unfollow", HttpMethod.POST, HttpEntity(null, authHeaders1), String::class.java).apply {
                assertEquals(HttpStatus.BAD_REQUEST, statusCode)
            }
        }
    }
}