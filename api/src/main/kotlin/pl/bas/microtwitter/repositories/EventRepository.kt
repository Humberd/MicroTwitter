package pl.bas.microtwitter.repositories

import org.springframework.data.repository.CrudRepository
import pl.bas.microtwitter.dao.EventDAO

interface EventRepository : CrudRepository<EventDAO, Int> {

}