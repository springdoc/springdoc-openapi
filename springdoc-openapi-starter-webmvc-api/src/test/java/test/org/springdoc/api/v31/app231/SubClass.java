package test.org.springdoc.api.v31.app231;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Used to create a new application")
public class SubClass extends SuperClass {

    public SubClass(String name) {
        super(name);
    }

    @Override
    @Schema(description = "Overriding the description in sub class")
    public String getName() {
        return super.getName();
    }
}
