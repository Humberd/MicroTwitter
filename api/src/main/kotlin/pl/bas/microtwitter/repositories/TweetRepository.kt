package pl.bas.microtwitter.repositories

import org.intellij.lang.annotations.Language
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import pl.bas.microtwitter.dao.TweetDAO
import pl.bas.microtwitter.dao.UserDAO

interface TweetRepository : JpaRepository<TweetDAO, Long> {
    fun findAllByUserLcusernameOrderByIdDesc(lcusername: String,
                                             pageable: Pageable): Page<TweetDAO>

    fun countByInReplyToTweet(inReplyToTweet: TweetDAO): Int
    fun findAllByInReplyToTweet(inReplyToTweet: TweetDAO,
                                pageable: Pageable): Page<TweetDAO>

    fun findAllByLikes_UserLcusername(username: String,
                                      pageable: Pageable): Page<TweetDAO>

    /**
    SELECT DISTINCT t.* FROM
        tweet t,
        (SELECT * FROM user_follower uf WHERE uf.user_id = 1) myFollower
    WHERE t.user_id = 1 OR t.user_id = myFollower.followed_user_id;
     */
    @Language("JPAQL")
    @Query("""
        select distinct t
        from TweetDAO t, UserDAO u
        join u.followedUsers fu
        where u = :user
        and (t.user = :user
        or t.user = fu)
        order by t.id desc
    """)
    fun findWall(@Param("user") user: UserDAO,
                 pageable: Pageable): Page<TweetDAO>
}