package pl.bas.microtwitter.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import pl.bas.microtwitter.dao.ParticipantDAO

interface ParticipantRepository : JpaRepository<ParticipantDAO, Int> {

}