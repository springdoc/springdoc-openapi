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

package test.org.springdoc.webmvc.scalar;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.core.io.support.SpringFactoriesLoader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The exclusion of Scalar's standalone autoconfiguration belongs to the Scalar starters: it is
 * only legitimate where a Springdoc Scalar controller replaces it (#3387). It is declared in this
 * module {@code META-INF/spring.factories}, and not in springdoc-openapi-starter-common, so that
 * it is loaded exactly when this starter is installed.
 *
 * @author awinishere
 */
class ScalarStarterRegistersTheDisableFilterTest {

	@Test
	void scalarStarterDisablesTheStandaloneAutoConfiguration() {

		SpringFactoriesLoader loader = SpringFactoriesLoader
				.forDefaultResourceLocation(getClass().getClassLoader());

		List<String> registeredFilters = loader.load(AutoConfigurationImportFilter.class)
				.stream()
				.map(filter -> filter.getClass().getName())
				.toList();

		assertThat(registeredFilters).contains("org.springdoc.scalar.ScalarDisableAutoConfiguration");
	}

}
