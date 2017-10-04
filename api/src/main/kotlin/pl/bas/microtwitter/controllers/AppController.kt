package pl.bas.microtwitter.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController()
@RequestMapping("/app")
class AppController {

    @GetMapping("/status", produces = arrayOf("text/html"))
    fun getStatus(): String {
        return """
            <h2>Status</h2>
            <div>Container Id: ${System.getenv("HOSTNAME")}</div>
            <div>Build number: ${System.getenv("BUILD_NO")}</div>
            <hr>
            <div>
                <pre>${System.getenv("COMMIT")}</pre>
            </div>
            """
    }

    @GetMapping("/ping")
    fun ping(): String {
        return "pong"
    }
}