package pl.bas.microtwitter.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.dto.SignupDTO
import pl.bas.microtwitter.repositories.UserRepository

@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired lateinit var userRepository: UserRepository

    @PostMapping("/signup")
    fun signup(@RequestBody body: SignupDTO): ResponseEntity<Unit> {
        val user = UserDAO().apply {
            username = body.username
            email = body.email
            fullName = body.fullName
            password = body.password
        }

        userRepository.save(user)

        return ResponseEntity.ok(Unit)
    }

    @PostMapping("login")
    fun login(): ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit)
    }

    @PostMapping("/password")
    fun updatePassword(): ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit)
    }
}