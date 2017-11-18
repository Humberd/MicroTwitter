package pl.bas.microtwitter.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController()
@RequestMapping("/app")
class AppController {

    /**
     * Returns a status of the backend application including:
     *  * Docker containerId
     *  * Jenkins build number
     *  * Git commit id
     */
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

    /**
     * Returns a hardcoded message to check if the service is alive
     */
    @GetMapping("/ping")
    fun ping(): String {
        return "pong"
    }
}