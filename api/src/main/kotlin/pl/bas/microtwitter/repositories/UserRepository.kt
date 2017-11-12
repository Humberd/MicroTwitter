package pl.bas.microtwitter.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import pl.bas.microtwitter.dao.UserDAO

interface UserRepository : JpaRepository<UserDAO, Long> {
    fun findByLcusername(lcusername: String): UserDAO?
    fun findAllByLcusernameContaining(lcusername: String,
                                      pageable: Pageable): Page<UserDAO>

    fun countByTweets_User(user: UserDAO): Int?
    fun countByLikes_User(user: UserDAO): Int?
    fun countByFollows_IsFollowedBy(user: UserDAO): Int?
    fun countByIsFollowedBy_Follows(user: UserDAO): Int?
}
