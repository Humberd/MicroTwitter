package pl.bas.microtwitter.dto

data class UpdatePasswordDTO(
        var oldPassword: String,
        var newPassword: String
)