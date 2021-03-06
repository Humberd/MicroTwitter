package pl.bas.microtwitter.security

import com.google.gson.Gson
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.access.expression.SecurityExpressionOperations
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.FilterInvocation
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import pl.bas.microtwitter.security.SecurityConstants.APP_STATUS_URLS
import pl.bas.microtwitter.security.SecurityConstants.LOGIN_URL
import pl.bas.microtwitter.security.SecurityConstants.SIGN_UP_URL
import pl.bas.microtwitter.security.SecurityConstants.SWAGGER_API_DOCS_URL
import pl.bas.microtwitter.security.SecurityConstants.SWAGGER_RESOURCES_URL
import pl.bas.microtwitter.security.SecurityConstants.SWAGGER_UI_URL
import pl.bas.microtwitter.security.SecurityConstants.SWAGGER_UI_URL2

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurity(private val userDetailsService: UserDetailsService,
                  private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                  private val gson: Gson) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable().authorizeRequests()
//                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.GET,
                        SWAGGER_API_DOCS_URL,
                        SWAGGER_RESOURCES_URL,
                        SWAGGER_UI_URL,
                        SWAGGER_UI_URL2
                ).permitAll()
//                .antMatchers(APP_STATUS_URLS).permitAll()
//                .anyRequest().authenticated()
                .and()
                .addFilter(JWTAuthenticationFilter(authenticationManager(), gson))
                .addFilter(JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    public override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfig = CorsConfiguration()
                .applyPermitDefaultValues().apply {
            addAllowedHeader(SecurityConstants.HEADER_STRING)
            addExposedHeader(SecurityConstants.HEADER_STRING)
            addAllowedMethod(HttpMethod.GET)
            addAllowedMethod(HttpMethod.POST)
            addAllowedMethod(HttpMethod.PUT)
            addAllowedMethod(HttpMethod.DELETE)
            addAllowedMethod(HttpMethod.HEAD)
            addAllowedMethod(HttpMethod.OPTIONS)
        }

        source.registerCorsConfiguration("/**", corsConfig)
        return source
    }
}