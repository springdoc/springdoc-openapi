package test.org.springdoc.api.v31.app8;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "the foo bar", deprecated = true)
public record FooBar(
		@Schema(description = "foo", requiredMode = REQUIRED, examples = { "1", "2" })
		int foo
) {
}
