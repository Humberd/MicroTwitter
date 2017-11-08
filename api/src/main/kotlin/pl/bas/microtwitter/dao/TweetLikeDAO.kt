package pl.bas.microtwitter.dao

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "tweetLike")
class TweetLikeDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @NotNull
    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null

    @NotNull
    @ManyToOne()
    var tweet: TweetDAO? = null

    @NotNull
    @ManyToOne()
    var user: UserDAO? = null

    @PrePersist
    protected fun onCreate() {
        createdAt = Date()
    }
}