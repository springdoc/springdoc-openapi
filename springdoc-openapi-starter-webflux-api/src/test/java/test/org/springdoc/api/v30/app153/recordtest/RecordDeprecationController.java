package test.org.springdoc.api.v30.app153.recordtest;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "record-deprecation-test")
public class RecordDeprecationController {

    @PostMapping("/items1")
    public ResponseEntity<String> addItem1(@RequestBody DTO1 dto1) {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/items2")
    public ResponseEntity<String> addItem2(@RequestBody DTO2 dto2) {
        return ResponseEntity.ok("ok");
    }

    public record DTO1(@Deprecated @NotNull String field) {}
    public record DTO2(@NotNull String field) {}
}
