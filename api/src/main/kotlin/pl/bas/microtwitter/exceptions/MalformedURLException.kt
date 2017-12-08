package pl.bas.microtwitter.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseBody
@ResponseStatus(HttpStatus.BAD_REQUEST)
class MalformedURLException(message: String?) : Exception(message)