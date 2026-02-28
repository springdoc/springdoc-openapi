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

package test.org.springdoc.api.v31.app200.config;

/**
 * The type Api version parser.
 *
 * @author bnasslahsen
 */
public class ApiVersionParser implements org.springframework.web.accept.ApiVersionParser {

	private static final String DEFAULT_VERSION = "1.0";

	// allows us to use /api/v2/users instead of /api/2.0/users
	@Override
	public Comparable parseVersion(String version) {
		// Remove "v" prefix if it exists (v1 becomes 1, v2 becomes 2)
		if (version.startsWith("v") || version.startsWith("V")) {
			version = version.substring(1);
		}
		// For non-numeric path segments (e.g. "api-docs", "swagger-ui.css"),
		// fall back to the default version to avoid NPE from returning null
		// in the reactive ApiVersionStrategy pipeline
		try {
			Double.parseDouble(version);
		}
		catch (NumberFormatException ex) {
			return DEFAULT_VERSION;
		}
		return version;
	}

}
