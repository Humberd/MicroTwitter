package pl.bas.microtwitter.dbmodels

import javax.persistence.*

@Entity
@Table(name = "events")
data class Event(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Int,

        @Column(name = "name")
        var name: String
)