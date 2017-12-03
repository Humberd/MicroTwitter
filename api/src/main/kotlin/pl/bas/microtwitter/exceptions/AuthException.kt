package pl.bas.microtwitter.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseBody
@ResponseStatus(HttpStatus.UNAUTHORIZED)
class AuthException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}