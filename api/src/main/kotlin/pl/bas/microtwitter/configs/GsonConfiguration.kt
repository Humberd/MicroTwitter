package pl.bas.microtwitter.configs

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import mu.KLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GsonConfiguration {
    companion object : KLogging()

    @Bean
    fun gson(): Gson = GsonBuilder()
            .setPrettyPrinting()
            .create()
}

