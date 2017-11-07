package pl.bas.microtwitter.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import pl.bas.microtwitter.repositories.UserRepository

import java.util.Collections.emptyList

@Service
class UserDetailsServiceImpl(private val applicationUserRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val applicationUser = applicationUserRepository.findByLcusername(username.toLowerCase()) ?: throw UsernameNotFoundException(username)
        return User(applicationUser.username, applicationUser.password, emptyList<GrantedAuthority>())
    }
}