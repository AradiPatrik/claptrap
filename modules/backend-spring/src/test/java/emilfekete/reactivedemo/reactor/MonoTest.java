package emilfekete.reactivedemo.reactor;

import jdk.jshell.JShell;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.*;
import reactor.test.StepVerifier;



public class MonoTest {

    @Test
    public void mono(){
        var data = "data";
        Mono<String> mono = Mono.just(data);

        StepVerifier.create(mono)
                .expectNext(data)
                .verifyComplete();
    }

    @Test
    public void map(){
        var data = "data";
        var mono = Mono.just(data)
                .map(String::length);

        StepVerifier.create(mono)
                .expectNext(data.length())
                .verifyComplete();
    }

    @Test
    public void error(){
        var data = "data";
        var mono = Mono.just(data).
                map(e -> {throw new RuntimeException();});


        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void error1(){
        var mono = Mono.error(RuntimeException::new);

        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void empty(){
        var mono = Mono.empty();

        StepVerifier.create(mono)
                .expectComplete()
                .verify();
    }

    @Test
    public void subscribe(){
        var mono = Mono.empty();

        mono.subscribe();

        mono.subscribe(data -> {});

        mono.subscribe(data -> {}, err -> {});

        mono.subscribe(data -> {}, err -> {}, () -> {});

        mono.subscribe(data -> {}, err -> {}, () -> {}, subscription -> subscription.request(1));
    }

    @Test
    public void doOnMethods(){
        var mono = Mono.empty()
                .doOnNext(data -> {})
                .doOnRequest(value -> {})
                .doOnSubscribe(subscription -> subscription.request(1))
                .doOnCancel(() -> {});

        mono.subscribe(data -> {}, Throwable::printStackTrace, () -> {}, Subscription::cancel);
    }

    @Test
    public void doOnErrorMethods(){
        var mono = Mono.empty()
                .doOnError(data -> {})
                .doOnError(IllegalArgumentException.class, err -> {})
                .doOnError(err -> err instanceof Exception, err -> {})
                .onErrorResume(err -> Mono.error(RuntimeException::new))
                .onErrorReturn("value");

        mono.subscribe(data -> {}, Throwable::printStackTrace, () -> {}, Subscription::cancel);
    }


}
