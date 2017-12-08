package pl.bas.microtwitter.dao

import pl.bas.microtwitter.exceptions.InvalidColorException
import pl.bas.microtwitter.helpers.buildValidUrl
import pl.bas.microtwitter.helpers.isHexColor
import javax.persistence.*

@Entity
@Table(name = "profile",
        indexes = arrayOf(Index(name = "fullNameIndex", columnList = "fullnameLc", unique = false)))
class ProfileDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "fullName", nullable = false)
    var fullName: String? = null
    @Column(nullable = false)
    var fullnameLc: String? = null

    @Column(name = "description", nullable = false)
    var description: String? = null

    @Column(name = "location", nullable = false)
    var location: String? = null

    @Column(name = "profileLinkColor", nullable = false)
    var profileLinkColor: String? = null

    @Column(name = "url", nullable = false)
    var url: String? = null

    @Column(name = "avatarUrl", nullable = false)
    var avatarUrl: String? = null

    @Column(name = "backgroundUrl", nullable = false)
    var backgroundUrl: String? = null

    @OneToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "birthdateId")
    var birthdate: BirthdateDAO? = null

    @PrePersist
    @PreUpdate
    protected fun preUpdate() {
        fullName = fullName?.trim() ?: ""
        fullnameLc = fullName?.toLowerCase()
        description = description?.trim() ?: ""
        location = location?.trim() ?: ""
        profileLinkColor = profileLinkColor?.trim() ?: "#1b95e0"
        url = url?.trim() ?: ""
        avatarUrl = avatarUrl?.trim() ?: ""
        backgroundUrl = backgroundUrl?.trim() ?: ""

        if (!isHexColor(profileLinkColor!!)) {
            throw InvalidColorException("Invalid color format. Allowed format: '#12a04b'")
        }

        if (url?.isNotEmpty()!!) {
            url = buildValidUrl(url)
        }
        if (avatarUrl?.isNotEmpty()!!) {
            avatarUrl = buildValidUrl(avatarUrl)
        }
        if (backgroundUrl?.isNotEmpty()!!) {
            backgroundUrl = buildValidUrl(backgroundUrl)
        }
    }
}