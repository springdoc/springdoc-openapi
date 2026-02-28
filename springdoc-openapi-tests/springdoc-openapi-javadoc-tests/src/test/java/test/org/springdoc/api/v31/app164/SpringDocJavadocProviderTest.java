/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v31.app164;

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