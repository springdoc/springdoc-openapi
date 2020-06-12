/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
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

package org.springdoc.security;

import java.util.Map;

import org.springdoc.core.SecurityOAuth2Provider;

import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

/**
 * The type Spring security o auth 2 provider.
 * @author bnasslahsen
 */
public class SpringSecurityOAuth2Provider implements SecurityOAuth2Provider {

	/**
	 * The Oauth 2 endpoint handler mapping.
	 */
	private FrameworkEndpointHandlerMapping oauth2EndpointHandlerMapping;

	/**
	 * Instantiates a new Spring security o auth 2 provider.
	 *
	 * @param oauth2EndpointHandlerMapping the oauth 2 endpoint handler mapping
	 */
	public SpringSecurityOAuth2Provider(FrameworkEndpointHandlerMapping oauth2EndpointHandlerMapping) {
		this.oauth2EndpointHandlerMapping = oauth2EndpointHandlerMapping;
	}

	/**
	 * Gets oauth 2 endpoint handler mapping.
	 *
	 * @return the oauth 2 endpoint handler mapping
	 */
	public FrameworkEndpointHandlerMapping getOauth2EndpointHandlerMapping() {
		return oauth2EndpointHandlerMapping;
	}

	@Override
	public Map<RequestMappingInfo, HandlerMethod> getHandlerMethods() {
		return oauth2EndpointHandlerMapping.getHandlerMethods();
	}

	@Override
	public Map getFrameworkEndpoints() {
		return oauth2EndpointHandlerMapping.getApplicationContext().getBeansWithAnnotation(FrameworkEndpoint.class);
	}

}
