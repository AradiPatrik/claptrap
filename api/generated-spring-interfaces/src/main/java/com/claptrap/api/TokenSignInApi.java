/**
* NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.1.0).
* https://openapi-generator.tech
* Do not edit the class manually.
*/
package com.claptrap.api;

import com.claptrap.model.DummyWire;
import io.swagger.annotations.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.server.ServerWebExchange;
    import reactor.core.publisher.Flux;
    import reactor.core.publisher.Mono;
    import org.springframework.http.codec.multipart.Part;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;
    import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-05-01T18:46:15.460440100+02:00[Europe/Budapest]")
@Api(value = "token-sign-in", description = "the token-sign-in API")
    public interface TokenSignInApi {

            /**
            * PUT /token-sign-in : Validates the token, and initializes the user identified by the sub claim if the user is not already present inside the database. This token can come from either the Google, or the Firebase sign in provider. 
            *
            * @return Validation successful. User was already present in the database. (status code 200)
                *         or Validation successful. User was created. (status code 201)
                *         or Token validation failed. The token is either faulty or expired. (status code 401)
            */
            @ApiOperation(value = "Validates the token, and initializes the user identified by the sub claim if the user is not already present inside the database. This token can come from either the Google, or the Firebase sign in provider. ", nickname = "tokenSignIn", notes = "", response = DummyWire.class, authorizations = {
        
        @Authorization(value = "bearerAuth")
         }, tags={  })
            @ApiResponses(value = { 
                @ApiResponse(code = 200, message = "Validation successful. User was already present in the database.", response = DummyWire.class),
                @ApiResponse(code = 201, message = "Validation successful. User was created.", response = DummyWire.class),
                @ApiResponse(code = 401, message = "Token validation failed. The token is either faulty or expired.") })
            @PutMapping(
            value = "/token-sign-in",
            produces = { "application/json" }
            )
        Mono<DummyWire> tokenSignIn(@AuthenticationPrincipal Mono<Jwt> jwt);
        }
