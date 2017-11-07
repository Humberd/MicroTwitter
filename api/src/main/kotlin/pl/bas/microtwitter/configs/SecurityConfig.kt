//package pl.bas.microtwitter.configs
//
//import com.coldstart.Jwt.JWTAuthenticationFilter
//import com.coldstart.Jwt.JWTLoginFilter
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
//
//
////https://github.com/quangIO/spring-kotlin-jwt-sample/blob/master/src/main/kotlin/com/coldstart/Config/SecurityConfig.kt
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity
////@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
//class SecurityConfig : WebSecurityConfigurerAdapter() {
//    override fun configure(http: HttpSecurity) {
//        http.
//                csrf()
//                .disable()
//                .antMatcher("/**").authorizeRequests()
//                .antMatchers("/user/*", "/browser/**").permitAll()
//                .anyRequest().authenticated()
//                .antMatchers("/metrics").hasAuthority("ADMIN")
//                .and()
//                .addFilterBefore(JWTLoginFilter("/user/login", authenticationManager()),
//                        UsernamePasswordAuthenticationFilter::class.java)
//                .addFilterBefore(JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//    }
//}