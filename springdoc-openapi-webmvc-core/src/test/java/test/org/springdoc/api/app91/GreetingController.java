package test.org.springdoc.api.app91;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
@Tag(name = "Demo", description = "The Demo API")
public class GreetingController {

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "This API will return a random greeting.")
  public ResponseEntity<Greeting> sayHello() {
    return ResponseEntity.ok(new Greeting(RandomStringUtils.randomAlphanumeric(10)));
  }
}
