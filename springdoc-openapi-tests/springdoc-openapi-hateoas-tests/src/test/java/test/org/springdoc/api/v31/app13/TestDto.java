package test.org.springdoc.api.v31.app13;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;

/**
 * Parent DTO that extends RepresentationModel for HATEOAS support.
 * This class demonstrates the base schema that includes _links field
 * automatically added by Spring HATEOAS.
 */
@Schema(
        description = "Parent DTO extending RepresentationModel",
        subTypes = {ExtendedTestDto.class}
)
public class TestDto extends RepresentationModel<TestDto> {

    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
