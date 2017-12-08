package pl.bas.microtwitter.dao

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "userx")// need to name it like this because 'user' is a reserved keyword in postgres
class UserDAO {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @NotNull
    @Column(name = "createdAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null

    @Column(name = "username", nullable = false)
    var username: String? = null
    @Column(name = "usernameLc", nullable = false, unique = true)
    var usernameLc: String? = null

    @Column(name = "email", nullable = false)
    var email: String? = null
    @Column(name = "emailLc", nullable = false, unique = true)
    var emailLc: String? = null

    @Column(name = "password", nullable = false)
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
        usernameLc = username?.toLowerCase()
        emailLc = email?.toLowerCase()
    }
}
