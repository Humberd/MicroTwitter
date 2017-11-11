package pl.bas.microtwitter.configs

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import mu.KLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.bas.microtwitter.serializers.DateDeserializer
import pl.bas.microtwitter.serializers.DateSerializer
import java.util.*

@Configuration
class GsonConfiguration {
    companion object : KLogging()

    @Bean
    fun gson(): Gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Date::class.java, DateSerializer())
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .create()
}

