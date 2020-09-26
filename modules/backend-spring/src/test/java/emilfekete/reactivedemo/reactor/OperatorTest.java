package emilfekete.reactivedemo.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.test.StepVerifier;

public class OperatorTest {

    @Test
    public void map(){
        var flux = Flux.just("aaa", "bbb", "ccc")
                .map(String::toUpperCase);

        StepVerifier.create(flux)
                .expectNext("AAA", "BBB", "CCC")
                .verifyComplete();
    }

    @Test
    public void flatMap(){
        var flux = Flux.just("aa", "bb", "cc")
                .flatMap(e -> Flux.just(e.split("")));

        StepVerifier.create(flux)
                .expectNext("a", "a", "b", "b", "c", "c")
                .verifyComplete();
    }

    @Test
    public void filter(){
        var flux = Flux.just("aaa", "bb", "ccc")
                .filter(e -> e.length() > 2);

        StepVerifier.create(flux)
                .expectNext("aaa", "ccc")
                .verifyComplete();
    }

    @Test
    public void groupBy(){
        var flux = Flux.just("a1", "a2", "b1", "b2")
                .groupBy(s -> s.charAt(0))
                .map(GroupedFlux::key)
                .map(Object::toString);

        StepVerifier.create(flux)
                .expectNext("a", "b")
                .verifyComplete();
    }

    @Test
    public void sort(){
        var flux = Flux.just("b", "a", "d", "c")
                .sort(String::compareTo);

        StepVerifier.create(flux)
                .expectNext("a", "b", "c", "d")
                .verifyComplete();
    }
}
