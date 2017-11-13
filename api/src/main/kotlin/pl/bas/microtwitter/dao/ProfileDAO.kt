package pl.bas.microtwitter.dao

import javax.persistence.*

@Entity
@Table(name = "profile")
class ProfileDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "fullName")
    var fullName: String? = ""

    @Column(name = "description")
    var description: String? = ""

    @Column(name = "location")
    var location: String? = ""

    @Column(name = "profileLinkColor")
    var profileLinkColor: String? = "#1b95e0"

    @Column(name = "url")
    var url: String? = ""

    @OneToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "birthdateId")
    var birthdate: BirthdateDAO? = null
}