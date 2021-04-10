package emilfekete.claptrap.backend.web.routes;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.function.Function;

@RestController
class Controller {
  @GetMapping("/token-sign-in")
  public Mono<String> getTokenSignIn() {
    return ReactiveSecurityContextHolder.getContext()
      .map(SecurityContext::getAuthentication)
      .map(Authentication::getPrincipal)
      .map(this::getUserDetailsStringFunction);
  }

  @NotNull
  private int getUserDetailsStringFunction(Object userDetails) {
    return Integer.valueOf(userDetails);
  }


}