package pl.bas.microtwitter.dao

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.persistence.PrePersist

@Entity
@Table(name = "tweet")
class TweetDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @NotNull
    @Column(name = "content")
    var content: String? = null

    @NotNull
    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null

    @OneToMany(mappedBy = "tweet", cascade = arrayOf(CascadeType.ALL))
    @OrderColumn(name = "id")
    var likes: List<TweetLikeDAO> = emptyList()

    @NotNull
    @ManyToOne
    var user: UserDAO? = null

    @PrePersist
    protected fun onCreate() {
        createdAt = Date()
    }
}