package pl.bas.microtwitter.dao

import javax.persistence.*


@Entity
@Table(name = "product")
data class Product(
        var name: String = "",

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "company_id")
        var company: Company? = null,

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
) {
    override fun toString(): String {
        return "{name: ${name}, company: ${company?.name}}"
    }
}