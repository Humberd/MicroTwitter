package pl.bas.microtwitter.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.bas.microtwitter.dao.Company
import pl.bas.microtwitter.dao.EventDAO
import pl.bas.microtwitter.dao.ParticipantDAO
import pl.bas.microtwitter.dao.Product
import pl.bas.microtwitter.repositories.CompanyRepository
import pl.bas.microtwitter.repositories.EventRepository
import pl.bas.microtwitter.repositories.ParticipantRepository
import pl.bas.microtwitter.repositories.ProductRepository

@RestController
@RequestMapping("/test")
class TestController {
    @Autowired lateinit var eventRepository: EventRepository
    @Autowired lateinit var participantRepository: ParticipantRepository

    @GetMapping("/events")
    fun events(): MutableIterable<EventDAO>? {
        return eventRepository.findAll()
    }

    @GetMapping("/addevents")
    fun addEvents() {
        val event = EventDAO(title = "foo",
                description = "bar")

        eventRepository.save(event)

        val participant1 = ParticipantDAO(name = "Adam", event = event)
        val participant2 = ParticipantDAO(name = "Tomasz", event = event)

        participantRepository.saveAll(setOf(participant1, participant2))

    }

    @Autowired
    lateinit var companyRepository: CompanyRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    @RequestMapping("/save/1")
    fun save(): String {
        // clear data
        productRepository.deleteAll()
        companyRepository.deleteAll()

        // prepare Company data
        val apple = Company("Apple")
        val samsung = Company("Samsung")

        // save list of companies to database
        companyRepository.saveAll(setOf(apple, samsung))

        // prepare Product data
        val iphone7 = Product("Iphone 7", apple)
        val iPadPro = Product("IPadPro", apple)

        val galaxyJ7 = Product("GalaxyJ7", samsung)
        val galaxyTabA = Product("GalaxyTabA", samsung)

        // save list of products to database
        productRepository.saveAll(setOf(iphone7, iPadPro, galaxyJ7, galaxyTabA))

        return "saving with approach 1 - done!"
    }

    @RequestMapping("/save/2")
    fun save2(): String {
        // clear data
        productRepository.deleteAll()
        companyRepository.deleteAll()

        // prepare Company data
        val apple = Company("Apple")
        val samsung = Company("Samsung")

        // prepare Product data
        val iphone7 = Product("Iphone 7", apple)
        val iPadPro = Product("IPadPro", apple)

        val galaxyJ7 = Product("GalaxyJ7", samsung)
        val galaxyTabA = Product("GalaxyTabA", samsung)

        // set products for companies
        apple.products = listOf(iphone7, iPadPro)
        samsung.products = listOf(galaxyJ7, galaxyTabA)

        // save list of companies to database
        companyRepository.saveAll(listOf(apple, samsung))

        return "saving with approach 2 - done!"
    }

    @RequestMapping("/companies")
    fun findAllCompanies(): MutableList<Company>? {

        return companyRepository.findAll()
    }

    @RequestMapping("/products")
    fun findAllProducts(): MutableList<Product>? {

        return productRepository.findAll()
    }
}