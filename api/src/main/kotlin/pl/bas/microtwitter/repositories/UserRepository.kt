package pl.bas.microtwitter.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.bas.microtwitter.dao.UserDAO

interface UserRepository : JpaRepository<UserDAO, Long> {
    fun findByLcusername(lcusername: String): UserDAO?
    fun countByTweets_User(user: UserDAO): Int?
}
