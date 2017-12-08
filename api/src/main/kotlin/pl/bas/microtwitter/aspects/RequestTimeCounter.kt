package pl.bas.microtwitter.aspects

import mu.KLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class RequestTimeCounter {
    companion object : KLogging()

    @Around("execution(@org.springframework.web.bind.annotation.* * *(..))")
    fun handleException(point: ProceedingJoinPoint): Any {
        val start = System.currentTimeMillis()
        return point.proceed().apply {
            val end = System.currentTimeMillis()
            logger.info { "Response time: ${end - start}ms for method ${point.signature.name}()" }
        }
    }

}
