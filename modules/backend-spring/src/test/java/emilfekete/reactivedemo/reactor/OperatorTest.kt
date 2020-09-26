package emilfekete.reactivedemo.reactor

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.GroupedFlux
import reactor.test.StepVerifier

class OperatorTest {
    @Test
    fun map() {
        val flux = Flux.just("aaa", "bbb", "ccc")
            .map { obj: String -> obj.toUpperCase() }
        StepVerifier.create(flux)
            .expectNext("AAA", "BBB", "CCC")
            .verifyComplete()
    }

    @Test
    fun flatMap() {
        val flux = Flux.just("aa", "bb", "cc")
            .flatMap { e: String -> Flux.just(*e.split("").toTypedArray()) }
        StepVerifier.create(flux)
            .expectNext("a", "a", "b", "b", "c", "c")
            .verifyComplete()
    }

    @Test
    fun filter() {
        val flux = Flux.just("aaa", "bb", "ccc")
            .filter { e: String -> e.length > 2 }
        StepVerifier.create(flux)
            .expectNext("aaa", "ccc")
            .verifyComplete()
    }

    @Test
    fun groupBy() {
        val flux = Flux.just("a1", "a2", "b1", "b2")
            .groupBy { s: String -> s[0] }
            .map { obj: GroupedFlux<Char, String> -> obj.key() }
            .map { obj: Char -> obj.toString() }
        StepVerifier.create(flux)
            .expectNext("a", "b")
            .verifyComplete()
    }

    @Test
    fun sort() {
        val flux = Flux.just("b", "a", "d", "c")
            .sort { obj: String, anotherString: String? ->
                obj.compareTo(
                    anotherString!!
                )
            }
        StepVerifier.create(flux)
            .expectNext("a", "b", "c", "d")
            .verifyComplete()
    }
}