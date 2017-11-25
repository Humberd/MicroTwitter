package pl.bas.microtwitter.dao

import pl.bas.microtwitter.exceptions.InvalidColorException
import pl.bas.microtwitter.helpers.isHexColor
import javax.persistence.*

@Entity
@Table(name = "profile",
        indexes = arrayOf(Index(name = "fullNameIndex", columnList = "lcfullName", unique = false)))
class ProfileDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "fullName", nullable = false)
    var fullName: String? = null
    @Column(nullable = false)
    var lcfullName: String? = null

    @Column(name = "description", nullable = false)
    var description: String? = null

    @Column(name = "location", nullable = false)
    var location: String? = null

    @Column(name = "profileLinkColor", nullable = false)
    var profileLinkColor: String? = "#1b95e0"

    @Column(name = "url", nullable = false)
    var url: String? = null

    @OneToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "birthdateId")
    var birthdate: BirthdateDAO? = null

    @PrePersist
    @PreUpdate
    protected fun preUpdate() {
        fullName = fullName ?: ""
        lcfullName = fullName?.toLowerCase()
        description = description ?: ""
        location = location ?: ""
        profileLinkColor = profileLinkColor ?: ""
        url = url ?: ""

        if (!isHexColor(profileLinkColor!!)) {
            throw InvalidColorException("Invalid color format. Allowed format: '#12a04b'")
        }
    }
}