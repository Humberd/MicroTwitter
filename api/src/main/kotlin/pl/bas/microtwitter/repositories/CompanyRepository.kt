package pl.bas.microtwitter.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.bas.microtwitter.dao.Company


@Repository
interface CompanyRepository : JpaRepository<Company, Long> {
}