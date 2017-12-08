package pl.bas.microtwitter.configs

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class PostgresExceptionResolverConfiguration : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = arrayOf(DataIntegrityViolationException::class))
    protected fun postgresHandler(ex: DataIntegrityViolationException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, ErrorResp(ex.cause?.cause?.message), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    data class ErrorResp(
            val message: String?
    )
}