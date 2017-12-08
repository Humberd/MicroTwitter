package pl.bas.microtwitter.repositories

import org.intellij.lang.annotations.Language
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import pl.bas.microtwitter.dao.UserDAO

interface UserRepository : JpaRepository<UserDAO, Long> {
    fun findByUsernameLc(usernameLc: String): UserDAO?

    @Language("JPAQL")
    @Query("""
        select u
        from UserDAO as u
        where u.usernameLc like concat('%',:usernameOrFullName,'%')
        or u.profile.fullnameLc like concat('%',:usernameOrFullName,'%')
        """)
    fun findByUsernameOrFullName(@Param("usernameOrFullName") usernameOrFullName: String,
                                 pageable: Pageable): Page<UserDAO>

    fun countByTweets_User(user: UserDAO): Int?
    fun countByLikes_User(user: UserDAO): Int?

    @Language("JPAQL")
    @Query("""
        select count(fu.id)
        from UserDAO u
        join u.followedUsers fu
        where u = :user
        """)
    fun countFollowedUsers(@Param("user") user: UserDAO): Int?

    @Language("JPAQL")
    @Query("""
        select count(fbu.id)
        from UserDAO u
        join u.followedByUsers fbu
        where u = :user
        """)
    fun countFollowers(@Param("user") user: UserDAO): Int?

    /**
     * Gets a list of users that a given user is following
     */
    @Language("JPAQL")
    @Query("""
        select fu
        from UserDAO u
        join u.followedUsers fu
        where u.usernameLc = :usernameLc
        """)
    fun findFollowedUsers(@Param("usernameLc") lcusername: String,
                          pageable: Pageable): Page<UserDAO>

    /**
     * Gets a list of users that a given user is being followed by
     */
    @Language("JPAQL")
    @Query("""
        select fbu
        from UserDAO u
        join u.followedByUsers fbu
        where u.usernameLc = :usernameLc
        """)
    fun findFollowers(@Param("usernameLc") lcusername: String,
                      pageable: Pageable): Page<UserDAO>
}
