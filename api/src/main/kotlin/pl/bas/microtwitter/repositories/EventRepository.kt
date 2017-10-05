package pl.bas.microtwitter.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import pl.bas.microtwitter.dao.EventDAO

interface EventRepository : JpaRepository<EventDAO, Int> {

}