package pl.bas.microtwitter.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.bas.microtwitter.dao.EventDAO
import pl.bas.microtwitter.dao.ParticipantDAO
import pl.bas.microtwitter.repositories.EventRepository
import pl.bas.microtwitter.repositories.ParticipantRepository

@RestController
@RequestMapping("/test")
class TestController {
    @Autowired lateinit var eventRepository: EventRepository
    @Autowired lateinit var participantRepository: ParticipantRepository

    @GetMapping("/events")
    fun events(): MutableIterable<EventDAO>? {
        eventRepository.save(
                EventDAO(title = "foo",
                        description = "bar",
                        participants = arrayOf(
                                ParticipantDAO(name = "Foobar")
                        )))

        return eventRepository.findAll()
    }

}