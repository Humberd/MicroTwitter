package pl.bas.microtwitter.dao

import org.springframework.data.annotation.Id
import javax.persistence.*

@Entity
@Table(name = "participant")
data class ParticipantDAO(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Int = 0,

        @Column(name = "name")
        var name: String,

        @ManyToOne
        @JoinColumn
        var event: EventDAO? = null
) {

}