package test.org.springdoc.api.app164;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.providers.SpringDocJavadocProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SpringDocJavadocProviderTest {
	private JavadocProvider javadocProvider;

	static Stream<Arguments> getFirstSentence() {
		return Stream.of(
				arguments(null, null),
				arguments("", ""),
				arguments("A b c. D e f", "A b c."),
				arguments("A b c", "A b c"),
				arguments("A b c<p>D e f", "A b c"),
				arguments("A b c. D<p>e f", "A b c"),
				arguments("A b c<p>D. e f", "A b c"),
				arguments("<p>A b c</p>D e f", "A b c"),
				arguments("<p>A b c. D</p>e f", "A b c"),
				arguments("A b c.d e f", "A b c.d e f")
		);
	}

	/**
	 * Edge cases not handled by the implementation.
	 */
	static Stream<Arguments> getFirstSentenceNotHandled() {
		return Stream.of(
				arguments("<p>A b c<p>d e f</p>", "A b c")
		);
	}

	@BeforeEach
	public void setup() {
		javadocProvider = new SpringDocJavadocProvider();
	}

	@ParameterizedTest
	@MethodSource
	public void getFirstSentence(String javadoc, String expectedFirstSentence) {
		assertThat(javadocProvider.getFirstSentence(javadoc))
				.isEqualTo(expectedFirstSentence);
	}

	@ParameterizedTest
	@MethodSource
	public void getFirstSentenceNotHandled(String javadoc, String correctFirstSentence) {
		assertThat(javadocProvider.getFirstSentence(javadoc))
				.isNotEqualTo(correctFirstSentence);
	}

}