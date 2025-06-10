package test.org.springdoc.api.v31.app8;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "user info")
public record UserInfo(
		@Schema(description = "The user's name", requiredMode = REQUIRED, examples = { "Madoka", "Homura" })
		String name,
		@Schema(description = "The user's age", requiredMode = REQUIRED, examples = { "114", "514" })
		int age,
		@Schema(description = "The user's fooBar", requiredMode = REQUIRED)
		FooBar fooBar
) {
}
