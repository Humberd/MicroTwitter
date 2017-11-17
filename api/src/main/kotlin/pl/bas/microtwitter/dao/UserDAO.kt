package pl.bas.microtwitter.dao

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "userx")// need to name it like this because 'user' is a reserved keyword in postgres
class UserDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @NotNull
    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null

    @Column(name = "username")
    var username: String? = null
    @Column(name = "lcusername", unique = true)
    var lcusername: String? = null

    @Column(name = "email")
    var email: String? = null
    @Column(name = "lcemail", unique = true)
    var lcemail: String? = null

    @Column(name = "password")
    var password: String? = null

    @OneToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL), orphanRemoval = true)
    @JoinColumn(name = "profileId")
    var profile: ProfileDAO? = null

    @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL), orphanRemoval = true)
    @OrderColumn(name = "id")
    var tweets: List<TweetDAO> = emptyList()

    @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL), orphanRemoval = true)
    @OrderColumn(name = "id")
    var likes: List<TweetLikeDAO> = emptyList()

    @ManyToMany()
    @JoinTable(name = "userFollower",
            joinColumns = arrayOf(JoinColumn(name = "userId")),
            inverseJoinColumns = arrayOf(JoinColumn(name = "followedUserId"))
    )
    var followedUsers: List<UserDAO> = emptyList()

    @ManyToMany(mappedBy = "followedUsers")
    var followedByUsers: List<UserDAO> = emptyList()

    @PreUpdate
    protected fun onUpdate() {
        lowerCaseFields()
    }

    @PrePersist
    protected fun onCreate() {
        createdAt = Date()
        lowerCaseFields()
    }

    private fun lowerCaseFields() {
        lcusername = username?.toLowerCase()
        lcemail = email?.toLowerCase()
    }
}
