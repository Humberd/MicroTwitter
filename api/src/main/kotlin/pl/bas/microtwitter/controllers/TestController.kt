package pl.bas.microtwitter.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.bas.microtwitter.dbmodels.Event
import pl.bas.microtwitter.repositories.EventsRepository

@RestController
@RequestMapping("/test")
class TestController {
    @Autowired lateinit var eventsRepository: EventsRepository

    @GetMapping("/events")
    fun events(): MutableIterable<Event>? {
        return eventsRepository.findAll()
    }

}