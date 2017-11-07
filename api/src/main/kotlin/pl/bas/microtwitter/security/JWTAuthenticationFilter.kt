package pl.bas.microtwitter.security

import com.google.gson.Gson
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pl.bas.microtwitter.dto.LoginDTO
import pl.bas.microtwitter.security.SecurityConstants.EXPIRATION_TIME
import pl.bas.microtwitter.security.SecurityConstants.HEADER_STRING
import pl.bas.microtwitter.security.SecurityConstants.LOGIN_URL
import pl.bas.microtwitter.security.SecurityConstants.SECRET
import pl.bas.microtwitter.security.SecurityConstants.TOKEN_PREFIX
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(val authManager: AuthenticationManager,
                              val gson: Gson) : UsernamePasswordAuthenticationFilter() {

    init {
        setFilterProcessesUrl(LOGIN_URL)
    }

    override fun attemptAuthentication(req: HttpServletRequest,
                                       res: HttpServletResponse?): Authentication {
        try {
            val creds = gson.fromJson(InputStreamReader(req.inputStream), LoginDTO::class.java)

            return authManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            creds.username,
                            creds.password,
                            ArrayList<GrantedAuthority>())
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    override fun successfulAuthentication(req: HttpServletRequest,
                                          res: HttpServletResponse,
                                          chain: FilterChain?,
                                          auth: Authentication) {
        val token = Jwts.builder()
                .setSubject((auth.principal as User).username)
                .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact()

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token)
    }
}