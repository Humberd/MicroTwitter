package pl.bas.microtwitter.controllers

import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import pl.bas.microtwitter.dao.BirthdateDAO
import pl.bas.microtwitter.dao.ProfileDAO
import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.dto.LoginDTO
import pl.bas.microtwitter.dto.SignupDTO
import pl.bas.microtwitter.dto.UpdatePasswordDTO
import pl.bas.microtwitter.exceptions.BadRequestException
import pl.bas.microtwitter.repositories.UserRepository

@CrossOrigin
@RestController
@RequestMapping("/auth")
class AuthController(
        val userRepository: UserRepository,
        val bCryptPasswordEncoder: BCryptPasswordEncoder) {
    companion object : KLogging()

    /**
     * Signs up user
     */
    @PreAuthorize("isAnonymous()")
    @Transactional
    @PostMapping("/signup")
    fun signup(@RequestBody body: SignupDTO): ResponseEntity<Unit> {
        val user = UserDAO().apply {
            username = body.username
            email = body.email
            password = bCryptPasswordEncoder.encode(body.password)
            profile = ProfileDAO().apply {
                fullName = body.fullName
                birthdate = BirthdateDAO()
            }
        }
        userRepository.save(user)

        return ResponseEntity.ok(Unit)
    }

    /**
     * Logs in user and responds with ["Authorization"] header
     * Login is handled by JWT filters in a [pl.bas.microtwitter.security] package
     */
    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    fun login(@RequestBody body: LoginDTO): ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit)
    }

    /**
     * Updates user password
     */
    @PreAuthorize("isAuthenticated()")
    @Transactional
    @PostMapping("/password")
    fun updatePassword(@RequestBody body: UpdatePasswordDTO,
                       user: UserDAO): ResponseEntity<Unit> {
        if (!bCryptPasswordEncoder.matches(body.oldPassword, user.password)) {
            throw BadRequestException("Invalid password")
        }

        user.password = bCryptPasswordEncoder.encode(body.newPassword)
        return ResponseEntity.ok(Unit)
    }
}