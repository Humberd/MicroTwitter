package pl.bas.microtwitter.dao

import javax.persistence.*

@Entity
@Table(name = "userx")// need to name it like this because 'user' is a reserved keyword in postgres
class UserDAO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: String? = null

    @Column(name = "username")
    var username: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "fullName")
    var fullName: String? = null

    @Column(name = "password")
    var password: String? = null
}
