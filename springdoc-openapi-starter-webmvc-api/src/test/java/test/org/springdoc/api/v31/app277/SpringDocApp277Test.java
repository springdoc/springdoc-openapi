/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v31.app277;

import io.swagger.v3.core.converter.ModelConverters;
import org.junit.jupiter.api.AfterAll;
import org.springdoc.core.customizers.JavaNullablePropertyCustomizer;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocV31Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;

/**
 * When the {@link org.springdoc.core.customizers.JavaNullablePropertyCustomizer} is enabled (opt-in),
 * properties annotated with {@code @Nullable} are marked nullable.
 *
 * @author Mattias-Sehlstedt
 */
@TestPropertySource(properties = Constants.SPRINGDOC_JAVA_NULLABLE_PROPERTY_CUSTOMIZER_ENABLED + "=true")
public class SpringDocApp277Test extends AbstractSpringDocV31Test {

	/**
	 * The converter is registered in the JVM-wide {@link ModelConverters} singleton,
	 * so it must be removed to avoid leaking into other tests.
	 */
	@AfterAll
	static void removeJavaNullablePropertyCustomizer() {
		ModelConverters instance = ModelConverters.getInstance(true);
		instance.getConverters().stream()
				.filter(JavaNullablePropertyCustomizer.class::isInstance)
				.toList()
				.forEach(instance::removeConverter);
	}

	@SpringBootApplication
	static class SpringDocTestApp {
	}
}

