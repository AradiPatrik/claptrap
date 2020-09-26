package emilfekete.reactivedemo.reactor

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.reactivestreams.Subscription
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.lang.Runnable
import java.util.ArrayList
import java.util.List

class FluxTest {
    @Test
    fun flux() {
        val flux = Flux.just("1", "2", "3")
        StepVerifier.create(flux)
            .expectNext("1", "2", "3")
            .verifyComplete()
    }

    @Test
    fun range() {
        val flux = Flux.range(1, 3)
        StepVerifier.create(flux)
            .expectNext(1, 2, 3)
            .verifyComplete()
    }

    @Test
    fun rangeRequest() {
        val flux = Flux.range(1, 3)
        val list = ArrayList<Int>()
        flux.subscribe(
            { e: Int -> list.add(e) },
            { err: Throwable? -> },
            {}) { subscription: Subscription -> subscription.request(2) }
        Assertions.assertEquals(2, list.size)
    }

    @Test
    fun fromIterable() {
        val flux = Flux.fromIterable(listOf(1, 2, 3))
        StepVerifier.create(flux)
            .expectNext(1, 2, 3)
            .verifyComplete()
    }
}