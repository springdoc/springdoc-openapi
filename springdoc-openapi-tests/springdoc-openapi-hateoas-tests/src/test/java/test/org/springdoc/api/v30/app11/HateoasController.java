package test.org.springdoc.api.v30.app11;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Hateoas", description = "HATEOAS with allOf composition test")
public class HateoasController {

    @GetMapping(path = "/test-dto", produces = "application/json")
    @Operation(summary = "Get Test DTO", description = "Returns a TestDto with HATEOAS links")
    public TestDto getTestDto() {
        TestDto dto = new TestDto();
        dto.setField("test field value");
        return dto;
    }

    @GetMapping(path = "/extended-test-dto", produces = "application/json")
    @Operation(summary = "Get Extended Test DTO", description = "Returns an ExtendedTestDto with HATEOAS links")
    public ExtendedTestDto getExtendedTestDto() {
        ExtendedTestDto dto = new ExtendedTestDto();
        dto.setField("parent field value");
        dto.setOtherField("extended field value");
        return dto;
    }
}
