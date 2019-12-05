package test.org.springdoc.api.app65;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health" , description = "Health check / ping API")
@RestController
public class HelloController {

    @Operation(summary = "Check server status", description = "Check server status, will return 200 with simple string if alive. Do nothing else.")
    @GetMapping(value = { "/ping", "/health", "/" }, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Healthy");
    }

}