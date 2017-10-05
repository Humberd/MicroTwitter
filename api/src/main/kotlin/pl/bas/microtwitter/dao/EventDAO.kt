package pl.bas.microtwitter.dao

import javax.persistence.*

@Entity
@Table(name = "event")
data class EventDAO(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Int = 0,

        @Column(name = "title")
        var title: String,

        @Column(name = "description")
        var description: String,

        @OneToMany(mappedBy = "event")
        @OrderColumn(name = "id")
        var participants: Array<ParticipantDAO>
)