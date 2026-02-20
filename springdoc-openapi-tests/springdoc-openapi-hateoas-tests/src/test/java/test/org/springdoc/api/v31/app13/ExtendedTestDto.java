package test.org.springdoc.api.v31.app13;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Extended DTO that inherits from TestDto using allOf composition.
 * This class verifies that the fix for issue #3161 works correctly,
 * ensuring _links is not duplicated in the child schema.
 */
@Schema(
        description = "Extended DTO with allOf composition"
)
public class ExtendedTestDto extends TestDto {

    private String otherField;

    public String getOtherField() {
        return otherField;
    }

    public void setOtherField(String otherField) {
        this.otherField = otherField;
    }
}
