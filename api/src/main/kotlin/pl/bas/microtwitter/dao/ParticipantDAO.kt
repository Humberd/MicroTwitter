package pl.bas.microtwitter.dao

import javax.persistence.*

@Entity
@Table(name = "participant")
data class ParticipantDAO(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,

        @Column(name = "name")
        var name: String,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "event_id")
        var event: EventDAO? = null
)