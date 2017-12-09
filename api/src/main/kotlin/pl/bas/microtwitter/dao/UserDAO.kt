package pl.bas.microtwitter.dao

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank
import org.hibernate.validator.constraints.URL
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

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

    @NotBlank
    @Column(name = "username")
    var username: String? = null
    @NotBlank
    @Column(name = "usernameLc", unique = true)
    var usernameLc: String? = null

    @Email
    @NotBlank
    @Column(name = "email")
    var email: String? = null
    @Email
    @NotBlank
    @Column(name = "emailLc", unique = true)
    var emailLc: String? = null

    @NotBlank
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
        usernameLc = username?.toLowerCase()
        emailLc = email?.toLowerCase()
    }
}
