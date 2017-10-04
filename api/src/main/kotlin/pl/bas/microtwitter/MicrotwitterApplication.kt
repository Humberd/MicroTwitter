package pl.bas.microtwitter

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class MicrotwitterApplication

fun main(args: Array<String>) {
    SpringApplication.run(MicrotwitterApplication::class.java, *args)
}
