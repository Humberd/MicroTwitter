package pl.bas.microtwitter.configs

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import javax.validation.ConstraintViolationException


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class ExceptionResolvers : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = arrayOf(DataIntegrityViolationException::class))
    protected fun postgresHandler(ex: DataIntegrityViolationException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, ErrorResp(ex.cause?.cause?.message), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(value = arrayOf(ConstraintViolationException::class))
    protected fun hibernateHandler(ex: ConstraintViolationException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, ErrorResp(ex.message), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders?, status: HttpStatus?, request: WebRequest): ResponseEntity<Any> {
        val error = ValidationErrorBuilder.fromBindingErrors(ex.getBindingResult())
        return handleExceptionInternal(ex, error, HttpHeaders(), HttpStatus.BAD_REQUEST, request)

    }

    data class ErrorResp(
            val message: String?
    )
}

class ValidationError(val errorMessage: String) {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private val errors = ArrayList<String>()

    fun addValidationError(error: String) {
        errors.add(error)
    }

    fun getErrors(): List<String> {
        return errors
    }
}

object ValidationErrorBuilder {
    fun fromBindingErrors(errors: Errors): ValidationError {
        val error = ValidationError("Validation failed. " + errors.getErrorCount() + " error(s)")
        for (objectError in errors.getAllErrors()) {
            error.addValidationError(objectError.getDefaultMessage())
        }
        return error
    }
}

//http://blog.codeleak.pl/2013/09/request-body-validation-in-spring-mvc-3.2.html