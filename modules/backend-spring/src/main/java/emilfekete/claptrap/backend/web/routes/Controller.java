package emilfekete.claptrap.backend.web.routes;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
class Controller {
  @GetMapping("/token-sign-in")
  public Mono<String> getTokenSignIn(@AuthenticationPrincipal Mono<Jwt> jwt) {
    return jwt.map(this::getUserDetailsStringFunction);
  }

  @NotNull
  private String getUserDetailsStringFunction(Jwt jwt) {
    jwt.getSubject();
  }
}
