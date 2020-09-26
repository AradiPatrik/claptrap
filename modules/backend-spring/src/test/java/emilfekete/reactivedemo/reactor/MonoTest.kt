package emilfekete.reactivedemo.reactor

import org.junit.jupiter.api.Test
import org.reactivestreams.Subscription
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MonoTest {
    @Test
    fun mono() {
        val data = "data"
        val mono = Mono.just(data)
        StepVerifier.create(mono)
            .expectNext(data)
            .verifyComplete()
    }

    @Test
    fun map() {
        val data = "data"
        val mono = Mono.just(data)
            .map { it.length }
        StepVerifier.create(mono)
            .expectNext(data.length)
            .verifyComplete()
    }

    @Test
    fun error() {
        val data = "data"
        val mono = Mono.just(data).map<Any> { throw RuntimeException() }
        StepVerifier.create(mono)
            .expectError(RuntimeException::class.java)
            .verify()
    }

    @Test
    fun error1() {
        val mono = Mono.error<Any> { RuntimeException() }
        StepVerifier.create(mono)
            .expectError(RuntimeException::class.java)
            .verify()
    }

    @Test
    fun empty() {
        val mono = Mono.empty<Any>()
        StepVerifier.create(mono)
            .expectComplete()
            .verify()
    }

    @Test
    fun subscribe() {
        val mono = Mono.empty<Any>()
        mono.subscribe()
        mono.subscribe { }
        mono.subscribe({ }) { }
        mono.subscribe({ }, { }) {}
        mono.subscribe(
            { },
            { },
            {}) { subscription: Subscription -> subscription.request(1) }
    }

    @Test
    fun doOnMethods() {
        val mono = Mono.empty<Any>()
            .doOnNext { }
            .doOnRequest { }
            .doOnSubscribe { it.request(1) }
            .doOnCancel {}

        mono.subscribe(
            { },
            { it.printStackTrace() },
            {}) { it.cancel() }
    }

    @Test
    fun doOnErrorMethods() {
        val mono = Mono.empty<Any>()
            .doOnError { }
            .doOnError(IllegalArgumentException::class.java) { }
            .doOnError({ err: Throwable? -> err is Exception }) { }
            .onErrorResume { Mono.error { RuntimeException() } }
            .onErrorReturn("value")
        mono.subscribe(
            { },
            { it.printStackTrace() },
            {}) { it.cancel() }
    }
}