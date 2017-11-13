package pl.bas.microtwitter.dao

import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Entity
@Table(name = "birthdate")
class BirthdateDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "day")
    @Min(1)
    @Max(31)
    var day: Short? = null

    @Min(1)
    @Max(12)
    @Column(name = "month")
    var month: Short? = null

    @Min(1900)
    @Max(2017)
    @Column(name = "year")
    var year: Short? = null
}