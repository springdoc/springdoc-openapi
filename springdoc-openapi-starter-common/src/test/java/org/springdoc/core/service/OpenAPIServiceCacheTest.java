/*
 *
 *  *
 *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.core.service;

import java.util.Locale;
import java.util.Optional;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springdoc.core.properties.SpringDocConfigProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests that the per-locale OpenAPI cache is bounded. The cache key derives from the
 * client-supplied {@code Accept-Language} header, so it must never grow without limit.
 *
 * @author bnasslahsen
 */
class OpenAPIServiceCacheTest {

	private OpenAPIService openAPIService(SpringDocConfigProperties properties) {
		return new OpenAPIService(Optional.empty(), null, properties, null, Optional.empty(), Optional.empty(),
				Optional.empty());
	}

	/**
	 * A distinct, well-formed locale per index. The region has to be three digits: a
	 * two-digit one is not valid BCP 47 and would be dropped, collapsing every index onto
	 * the same cache key.
	 * @param index the index
	 * @return the locale
	 */
	private Locale locale(int index) {
		return Locale.forLanguageTag(String.format("xx-%03d", index));
	}

	@Test
	void evictsBeyondTheConfiguredMaxEntries() {
		SpringDocConfigProperties properties = new SpringDocConfigProperties();
		properties.getCache().setMaxEntries(3);
		OpenAPIService service = openAPIService(properties);

		for (int i = 0; i < 50; i++) {
			service.setCachedOpenAPI(new OpenAPI(), locale(i));
		}

		// the three most recently written locales survive, everything older is evicted
		assertNotNull(service.getCachedOpenAPI(locale(49)));
		assertNotNull(service.getCachedOpenAPI(locale(48)));
		assertNotNull(service.getCachedOpenAPI(locale(47)));
		assertNull(service.getCachedOpenAPI(locale(46)));
		assertNull(service.getCachedOpenAPI(locale(0)));
	}

	@Test
	void appliesADefaultBoundWhenUnconfigured() {
		SpringDocConfigProperties properties = new SpringDocConfigProperties();
		assertEquals(100, properties.getCache().getMaxEntries());
		OpenAPIService service = openAPIService(properties);

		for (int i = 0; i < 500; i++) {
			service.setCachedOpenAPI(new OpenAPI(), locale(i));
		}

		assertNotNull(service.getCachedOpenAPI(locale(499)));
		assertNull(service.getCachedOpenAPI(locale(399)));
	}

	@Test
	void aNonPositiveMaxEntriesFallsBackToTheDefaultRatherThanMeaningUnlimited() {
		SpringDocConfigProperties properties = new SpringDocConfigProperties();
		properties.getCache().setMaxEntries(0);
		assertEquals(100, properties.getCache().getMaxEntries());

		properties.getCache().setMaxEntries(-1);
		assertEquals(100, properties.getCache().getMaxEntries());
	}

	@Test
	void rewritingTheSameLocaleDoesNotConsumeCapacity() {
		SpringDocConfigProperties properties = new SpringDocConfigProperties();
		properties.getCache().setMaxEntries(2);
		OpenAPIService service = openAPIService(properties);

		for (int i = 0; i < 10; i++) {
			service.setCachedOpenAPI(new OpenAPI(), Locale.ENGLISH);
		}
		service.setCachedOpenAPI(new OpenAPI(), Locale.FRENCH);

		assertNotNull(service.getCachedOpenAPI(Locale.ENGLISH));
		assertNotNull(service.getCachedOpenAPI(Locale.FRENCH));
	}

}
