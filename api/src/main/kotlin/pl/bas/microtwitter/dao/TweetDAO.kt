package pl.bas.microtwitter.dao

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

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

    @OneToMany(mappedBy = "tweet", cascade = arrayOf(CascadeType.ALL), orphanRemoval = true)
    @OrderColumn(name = "id")
    var likes: List<TweetLikeDAO> = emptyList()

    @NotNull
    @ManyToOne
    var user: UserDAO? = null

    @OneToMany(mappedBy = "inReplyToTweet")
    @OrderColumn(name = "id")
    var comments: List<TweetDAO> = emptyList()

    @ManyToOne
    var inReplyToTweet: TweetDAO? = null

    @PrePersist
    protected fun onCreate() {
        createdAt = Date()
    }
}