package emilfekete.reactivedemo.reactor;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FluxTest {

    @Test
    public void flux(){
        var flux = Flux.just("1", "2", "3");

        StepVerifier.create(flux)
                .expectNext("1", "2", "3")
                .verifyComplete();
    }

    @Test
    public void range(){
        var flux = Flux.range(1,3);

        StepVerifier.create(flux)
                .expectNext(1,2,3)
                .verifyComplete();
    }

    @Test
    public void rangeRequest(){
        var flux = Flux.range(1,3);

        var list = new ArrayList<Integer>();
        flux.subscribe(list::add, err -> {}, () -> {}, subscription -> subscription.request(2));

        assertEquals(2, list.size());
    }

    @Test
    public void fromIterable(){
        var flux = Flux.fromIterable(List.of(1,2,3));

        StepVerifier.create(flux)
                .expectNext(1,2,3)
                .verifyComplete();
    }
}
