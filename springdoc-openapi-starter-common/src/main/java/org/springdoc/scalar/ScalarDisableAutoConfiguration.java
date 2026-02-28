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

/**
 * @author bnasslahsen
 */

import java.util.Set;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;

/**
 * Disable Scalar WebJar AutoConfiguration.
 */
public class ScalarDisableAutoConfiguration
		implements AutoConfigurationImportFilter {

	private static final Set<String> EXCLUDED = Set.of(
			"com.scalar.maven.webmvc.ScalarWebMvcAutoConfiguration",
			"com.scalar.maven.webflux.ScalarWebFluxAutoConfiguration"
	);

	@Override
	public boolean[] match(
			String[] candidates,
			AutoConfigurationMetadata metadata) {

		boolean[] matches = new boolean[candidates.length];

		for (int i = 0; i < candidates.length; i++) {
			String candidate = candidates[i];
			matches[i] = (candidate == null) || !EXCLUDED.contains(candidate);
		}

		return matches;
	}
}
