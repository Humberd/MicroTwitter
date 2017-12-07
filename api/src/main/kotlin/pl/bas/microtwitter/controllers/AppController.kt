package pl.bas.microtwitter.controllers

import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.PermitAll

@CrossOrigin
@RestController()
@RequestMapping("/app")
class AppController {

    /**
     * Returns a status of the backend application including:
     *  * Docker containerId
     *  * Jenkins build number
     *  * Git commit id
     */
    @PreAuthorize("permitAll()")
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
    @PreAuthorize("permitAll()")
    @GetMapping("/ping")
    fun ping(): String {
        return "pong"
    }
}