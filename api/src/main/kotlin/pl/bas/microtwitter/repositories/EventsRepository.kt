package pl.bas.microtwitter.repositories

import org.springframework.data.repository.CrudRepository
import pl.bas.microtwitter.dbmodels.Event

interface EventsRepository : CrudRepository<Event, Int> {

}