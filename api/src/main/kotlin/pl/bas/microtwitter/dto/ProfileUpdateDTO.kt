package pl.bas.microtwitter.dto

data class ProfileUpdateDTO(
        var fullName: String?,
        var description: String?,
        var location: String?,
        var profileLinkColor: String?,
        var url: String?,
        var birthdate: BirthdateDTO?
)

data class BirthdateDTO(
        var day: Short?,
        var month: Short?,
        var year: Short?
)