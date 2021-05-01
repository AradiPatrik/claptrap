package emilfekete.claptrap.backend.web.routes;

import com.claptrap.api.DummyApi;
import com.claptrap.api.GamesApi;
import com.claptrap.api.TokenSignInApi;
import com.claptrap.model.Game;
import com.claptrap.model.Move;
import emilfekete.claptrap.backend.db.entity.Customer;
import emilfekete.claptrap.backend.db.repository.CustomerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
class Controller implements TokenSignInApi, DummyApi {

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

  @Override
  public Mono<ResponseEntity<Flux<Object>>> dummyGet(ServerWebExchange exchange) {

  }

  @Override
  public Mono<ResponseEntity<Object>> tokenSignInPut(ServerWebExchange exchange) {
  }
}
