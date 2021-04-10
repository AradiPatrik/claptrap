package emilfekete.claptrap.backend.web.routes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Controller {
  @GetMapping("/token-sign-in")
  public String getTokenSignIn() {
    return "kaga";
  }
}