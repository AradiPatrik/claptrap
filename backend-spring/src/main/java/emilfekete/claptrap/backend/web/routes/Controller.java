package emilfekete.claptrap.backend.web.routes;

import emilfekete.claptrap.backend.db.entity.Customer;
import emilfekete.claptrap.backend.db.repository.CustomerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
class Controller {

  @Autowired
  private CustomerRepository customerRepository;

  @GetMapping("/token-sign-in")
  public Mono<String> getTokenSignIn(@AuthenticationPrincipal Mono<Jwt> jwt) {
    return jwt.map(this::getUserDetailsStringFunction);
  }

  @GetMapping("/users")
  public Flux<Customer> getUsers(@AuthenticationPrincipal Mono<Jwt> jwt) {
    return customerRepository.findAll();
  }

  @NotNull
  private String getUserDetailsStringFunction(Jwt jwt) {
    return jwt.getSubject();
  }
}
