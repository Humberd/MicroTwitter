package pl.bas.microtwitter.resolvers

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pl.bas.microtwitter.dao.UserDAO
import pl.bas.microtwitter.exceptions.AuthException
import pl.bas.microtwitter.repositories.UserRepository

class UserDAOResolver(val userRepository: UserRepository) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType.isAssignableFrom(UserDAO::class.java)
    }

    override fun resolveArgument(parameter: MethodParameter?, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest?, binderFactory: WebDataBinderFactory?): UserDAO? {
        val principal = webRequest?.userPrincipal
        if (principal === null) {
            if (parameter?.hasParameterAnnotation(Optional::class.java)!!) {
                return null
            }
            throw AuthException("Trying to resolve UserDAO parameter, but the principal is null")
        }

        val userDAO = userRepository.findByUsernameLc(principal.name.toLowerCase())
        if (userDAO === null) {
            throw AuthException("Trying to resolve UserDAO parameter, but there is no user with a name '${principal.name}'")
        }
        return userDAO
    }
}