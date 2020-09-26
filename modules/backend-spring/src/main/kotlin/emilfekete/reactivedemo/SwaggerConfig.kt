package emilfekete.reactivedemo

import org.reactivestreams.Publisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux

@Configuration
class SwaggerConfig {
    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Reactive Demo")
            .description("Reactive Demo desc")
            .version("1.0.0")
            .build()
    }

    @Bean
    fun docket(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .genericModelSubstitutes(Mono::class.java, Flux::class.java, Publisher::class.java)
            .apiInfo(apiInfo())
            .enable(true)
            .select()
            .paths(PathSelectors.any())
            .build()
    }
}