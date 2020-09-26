package emilfekete.reactivedemo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@SpringBootApplication
class ReactivedemoApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ReactivedemoApplication::class.java, *args)
        }
    }

}