package pl.bas.microtwitter.security 

object SecurityConstants {
    val SECRET = "SecretKeyToGenJWTs"
    val EXPIRATION_TIME: Long = 864000000 // 10 days
    val TOKEN_PREFIX = "Bearer "
    val HEADER_STRING = "Authorization"

    val SIGN_UP_URL = "/auth/signup"
    val LOGIN_URL = "/auth/login"
    val APP_STATUS_URLS = "/app/**"

    val SWAGGER_API_DOCS_URL = "/v2/api-docs"
    val SWAGGER_RESOURCES_URL = "/swagger-resources/**"
    val SWAGGER_UI_URL = "/swagger-ui.html"
    val SWAGGER_UI_URL2 = "/webjars/springfox-swagger-ui/**"
}