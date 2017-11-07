package pl.bas.microtwitter.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.dto.UpdatePasswordDTO
import pl.bas.microtwitter.dto.LoginDTO
import pl.bas.microtwitter.dto.SignupDTO
import pl.bas.microtwitter.repositories.UserRepository

@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Transactional
    @PostMapping("/signup")
    fun signup(@RequestBody body: SignupDTO): ResponseEntity<Unit> {
        val user = UserDAO().apply {
            username = body.username
            email = body.email
            fullName = body.fullName
            password = bCryptPasswordEncoder.encode(body.password)
        }

        userRepository.save(user)

        return ResponseEntity.ok(Unit)
    }

    @PostMapping("login")
    fun login(): ResponseEntity<Unit> {
        // Login is handled by JWT filters in a securty package
        return ResponseEntity.ok(Unit)
    }

    @PostMapping("/password")
    fun updatePassword(@RequestBody body: UpdatePasswordDTO): ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit)
    }
}