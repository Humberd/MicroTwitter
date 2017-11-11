package pl.bas.microtwitter.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import pl.bas.microtwitter.dao.TweetDAO

interface TweetRepository: JpaRepository<TweetDAO, Long> {
    fun findAllByUserLcusername(lcusername: String, pageable: Pageable): Page<TweetDAO>
}