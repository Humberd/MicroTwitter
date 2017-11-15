package pl.bas.microtwitter.dao

import pl.bas.microtwitter.exceptions.InvalidColorException
import pl.bas.microtwitter.helpers.isHexColor
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "profile")
class ProfileDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @NotNull
    @Column(name = "fullName")
    var fullName: String? = null

    @NotNull
    @Column(name = "description")
    var description: String? = null

    @NotNull
    @Column(name = "location")
    var location: String? = null

    @NotNull
    @Column(name = "profileLinkColor")
    var profileLinkColor: String? = "#1b95e0"

    @NotNull
    @Column(name = "url")
    var url: String? = null

    @OneToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "birthdateId")
    var birthdate: BirthdateDAO? = null

    @PrePersist
    @PreUpdate
    protected fun preUpdate() {
        fullName = fullName ?: ""
        description = description ?: ""
        location = location ?: ""
        profileLinkColor = profileLinkColor ?: ""
        url = url ?: ""

        if (!isHexColor(profileLinkColor!!)) {
            throw InvalidColorException("Invalid color format. Allowed format: '#12a04b'")
        }
    }
}