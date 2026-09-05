/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core.service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springdoc.core.properties.SpringDocConfigProperties;

import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests that the per-locale OpenAPI cache stays bounded, so that a caller varying the
 * {@code Accept-Language} header cannot grow the singleton service's memory footprint
 * without limit.
 *
 * @author bnasslahsen
 */
class OpenAPIServiceCacheTest {

	/**
	 * Builds a service whose cache holds at most the given number of locales.
	 *
	 * @param maxEntries the configured cache bound
	 * @return the service under test
	 */
	private OpenAPIService openApiService(int maxEntries) {
		SpringDocConfigProperties properties = new SpringDocConfigProperties();
		properties.getCache().setMaxEntries(maxEntries);
		return new OpenAPIService(Optional.empty(), null, properties, null, Optional.empty(), Optional.empty(),
				Optional.empty());
	}

	/**
	 * Reads the internal cache of the given service.
	 *
	 * @param openAPIService the service under test
	 * @return the cache map
	 */
	@SuppressWarnings("unchecked")
	private Map<String, OpenAPI> cacheOf(OpenAPIService openAPIService) {
		return (Map<String, OpenAPI>) ReflectionTestUtils.getField(openAPIService, "cachedOpenAPI");
	}

	/**
	 * Caching more locales than configured must evict, not grow.
	 */
	@Test
	void testCacheIsBoundedByTheConfiguredMaximum() {
		int maxEntries = 10;
		OpenAPIService openAPIService = openApiService(maxEntries);

		for (int i = 0; i < maxEntries * 20; i++) {
			openAPIService.setCachedOpenAPI(new OpenAPI(), Locale.forLanguageTag("en-x-v" + i));
		}

		assertThat(cacheOf(openAPIService)).hasSize(maxEntries);
	}

	/**
	 * Eviction must follow access order, so that the locales actually in use survive.
	 */
	@Test
	void testLeastRecentlyUsedLocaleIsEvictedFirst() {
		OpenAPIService openAPIService = openApiService(2);
		Locale kept = Locale.forLanguageTag("fr");
		Locale evicted = Locale.forLanguageTag("de");

		openAPIService.setCachedOpenAPI(new OpenAPI(), kept);
		openAPIService.setCachedOpenAPI(new OpenAPI(), evicted);
		openAPIService.getCachedOpenAPI(kept);
		openAPIService.setCachedOpenAPI(new OpenAPI(), Locale.forLanguageTag("it"));

		assertThat(openAPIService.getCachedOpenAPI(kept)).isNotNull();
		assertThat(openAPIService.getCachedOpenAPI(evicted)).isNull();
	}

	/**
	 * A non-positive configured bound must not disable the cache or throw.
	 */
	@Test
	void testNonPositiveMaximumKeepsAtLeastOneEntry() {
		OpenAPIService openAPIService = openApiService(0);
		Locale locale = Locale.forLanguageTag("fr");

		openAPIService.setCachedOpenAPI(new OpenAPI(), locale);

		assertThat(openAPIService.getCachedOpenAPI(locale)).isNotNull();
		assertThat(cacheOf(openAPIService)).hasSize(1);
	}

}
