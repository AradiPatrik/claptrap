package emilfekete.reactivedemo

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api")
class HomeController {
    @PostMapping("/images")
    fun postImages(@RequestBody images: Flux<Image>): Mono<Void> {
        return images.map { image: Image -> image }
            .then()
    }

    @GetMapping("/images")
    fun getImages(): Flux<Image> = Flux.just(Image())
}