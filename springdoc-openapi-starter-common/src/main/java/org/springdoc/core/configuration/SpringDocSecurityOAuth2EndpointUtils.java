/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package org.springdoc.core.configuration;

import java.util.Optional;

import org.springframework.security.web.SecurityFilterChain;

/**
 * The type Spring doc security o auth 2 endpoint utils.
 *
 * @param <T> the type parameter
 * @author bnasslahsen
 */
public final class SpringDocSecurityOAuth2EndpointUtils<T> {

	/**
	 * The Oauth 2 endpoint filter.
	 */
	private T oauth2EndpointFilter;

	/**
	 * Instantiates a new Spring doc security o auth 2 endpoint utils.
	 *
	 * @param oauth2EndpointFilter the oauth 2 endpoint filter
	 */
	public SpringDocSecurityOAuth2EndpointUtils(T oauth2EndpointFilter) {
		this.oauth2EndpointFilter = oauth2EndpointFilter;
	}

	/**
	 * Find endpoint object.
	 *
	 * @param filterChain the filter chain
	 * @return the object
	 */
	public Object findEndpoint(SecurityFilterChain filterChain) {
		Optional<?> oAuth2EndpointFilterOptional =
				 filterChain.getFilters().stream()
				   .filter(((Class <?>) oauth2EndpointFilter)::isInstance)
				   .map(((Class <?>) oauth2EndpointFilter)::cast)
				   .findAny();
		return oAuth2EndpointFilterOptional.orElse(null);
	}
}
