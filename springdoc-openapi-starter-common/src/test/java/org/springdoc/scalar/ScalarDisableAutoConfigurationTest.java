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

package org.springdoc.scalar;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.core.io.support.SpringFactoriesLoader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * springdoc-openapi-starter-common is pulled in by every starter: nothing that disables Scalar's
 * standalone autoconfiguration may be registered from here, otherwise applications using the
 * Swagger UI or the API-only starter lose the Scalar integration with nothing replacing it (#3387).
 * The filter itself must keep excluding both Scalar auto-configurations, since it is the guard
 * against the duplicate registration of #3084 once a Springdoc Scalar starter declares it.
 *
 * @author awinishere
 */
class ScalarDisableAutoConfigurationTest {

	private static final String FILTER_CLASS = "org.springdoc.scalar.ScalarDisableAutoConfiguration";

	private static final String[] SCALAR_AUTO_CONFIGURATIONS = {
			"com.scalar.maven.webmvc.ScalarWebMvcAutoConfiguration",
			"com.scalar.maven.webflux.ScalarWebFluxAutoConfiguration" };

	private List<String> registeredFilterNames() {
		SpringFactoriesLoader loader = SpringFactoriesLoader
				.forDefaultResourceLocation(getClass().getClassLoader());
		return loader.load(AutoConfigurationImportFilter.class)
				.stream()
				.map(filter -> filter.getClass().getName())
				.toList();
	}

	@Test
	void noScalarAutoConfigurationImportFilterIsRegisteredFromThisModule() {

		assertThat(registeredFilterNames()).doesNotContain(FILTER_CLASS);
	}

	@Test
	void filterStillDisablesBothScalarAutoConfigurations() {

		boolean[] matches = new ScalarDisableAutoConfiguration()
				.match(SCALAR_AUTO_CONFIGURATIONS, (AutoConfigurationMetadata) null);

		assertThat(matches).containsExactly(false, false);
	}

}
