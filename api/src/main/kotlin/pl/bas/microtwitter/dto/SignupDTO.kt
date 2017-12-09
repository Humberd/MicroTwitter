package pl.bas.microtwitter.dto

import org.hibernate.validator.constraints.NotBlank

data class SignupDTO(
        var username: String,
        var email: String,
        var fullName: String,
        var password: String
)

/**
 * Spring takes annotations only from class and not from data types
 */
class SignupDTOJ {
    var username: String? = null
    var email: String? = null
    var fullName: String? = null
    @NotBlank
    var password: String? = null
}