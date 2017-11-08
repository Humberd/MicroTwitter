package pl.bas.microtwitter.repositories

import org.springframework.data.jpa.repository.JpaRepository
import pl.bas.microtwitter.dao.TweetDAO

interface TweetRepository: JpaRepository<TweetDAO, Long>