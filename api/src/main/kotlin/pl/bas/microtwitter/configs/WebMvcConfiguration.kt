package pl.bas.microtwitter.configs

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pl.bas.microtwitter.repositories.UserRepository
import pl.bas.microtwitter.resolvers.UserDAOResolver

@Configuration
class WebMvcConfiguration(
        val userRepository: UserRepository) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>?) {
        resolvers?.add(UserDAOResolver(userRepository))
    }
}