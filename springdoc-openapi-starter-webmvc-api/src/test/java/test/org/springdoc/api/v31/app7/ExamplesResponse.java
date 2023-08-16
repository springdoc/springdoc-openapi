package test.org.springdoc.api.v31.app7;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record ExamplesResponse(
		@Schema(description = "name", requiredMode = REQUIRED, examples = { "name" })
		String name,
		@Schema(description = "subject", requiredMode = REQUIRED, example = "Hello", examples = { "Hello", "World" })
		String subject
) {}
