package pl.bas.microtwitter.controllers

import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.bas.microtwitter.dao.ProfileDAO
import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.dto.SignupDTO
import pl.bas.microtwitter.dto.UpdatePasswordDTO
import pl.bas.microtwitter.exceptions.BadRequestException
import pl.bas.microtwitter.repositories.UserRepository

@RestController
@RequestMapping("/auth")
class AuthController(
        val userRepository: UserRepository,
        val bCryptPasswordEncoder: BCryptPasswordEncoder) {
    companion object : KLogging()

    /**
     * Signs up user
     */
    @Transactional
    @PostMapping("/signup")
    fun signup(@RequestBody body: SignupDTO): ResponseEntity<Unit> {
        val user = UserDAO().apply {
            username = body.username
            email = body.email
            password = bCryptPasswordEncoder.encode(body.password)
            profile = ProfileDAO().apply {
                fullName = body.fullName
            }
        }
        userRepository.save(user)

        return ResponseEntity.ok(Unit)
    }

    /**
     * Logs in user and responds with ["Authorization"] header
     * Login is handled by JWT filters in a [pl.bas.microtwitter.security] package
     */
    @PostMapping("login")
    fun login(): ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit)
    }

    /**
     * Updates user password
     */
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