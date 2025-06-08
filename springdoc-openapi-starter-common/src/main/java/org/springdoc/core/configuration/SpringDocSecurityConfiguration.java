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

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.configuration.hints.SpringDocSecurityHints;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.OpenApiCustomizer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.pattern.PathPattern;

import static org.springdoc.core.utils.Constants.SPRINGDOC_SHOW_LOGIN_ENDPOINT;
import static org.springdoc.core.utils.Constants.SPRINGDOC_SHOW_OAUTH2_ENDPOINTS;
import static org.springdoc.core.utils.SpringDocUtils.getConfig;

/**
 * The type Spring doc security configuration.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnExpression("${springdoc.api-docs.enabled:true} and ${springdoc.enable-spring-security:true}")
@ConditionalOnClass(SecurityFilterChain.class)
@ConditionalOnWebApplication
@ConditionalOnBean(SpringDocConfiguration.class)
@ImportRuntimeHints(SpringDocSecurityHints.class)
public class SpringDocSecurityConfiguration {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDocSecurityConfiguration.class);

	static {
		getConfig().addRequestWrapperToIgnore(Authentication.class)
				.addResponseTypeToIgnore(Authentication.class)
				.addAnnotationsToIgnore(AuthenticationPrincipal.class);
	}

	/**
	 * The type Spring security login endpoint configuration.
	 */
	@Lazy(false)
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(jakarta.servlet.Filter.class)
	class SpringSecurityLoginEndpointConfiguration {

		/**
		 * Spring security login endpoint customiser open api customiser.
		 *
		 * @param applicationContext the application context
		 * @return the open api customiser
		 */
		@Bean
		@ConditionalOnProperty(SPRINGDOC_SHOW_LOGIN_ENDPOINT)
		@Lazy(false)
		OpenApiCustomizer springSecurityLoginEndpointCustomizer(ApplicationContext applicationContext) {
			FilterChainProxy filterChainProxy = applicationContext.getBean(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, FilterChainProxy.class);
			return openAPI -> {
				for (SecurityFilterChain filterChain : filterChainProxy.getFilterChains()) {
					Optional<UsernamePasswordAuthenticationFilter> optionalFilter =
							filterChain.getFilters().stream()
									.filter(UsernamePasswordAuthenticationFilter.class::isInstance)
									.map(UsernamePasswordAuthenticationFilter.class::cast)
									.findAny();
					Optional<DefaultLoginPageGeneratingFilter> optionalDefaultLoginPageGeneratingFilter =
							filterChain.getFilters().stream()
									.filter(DefaultLoginPageGeneratingFilter.class::isInstance)
									.map(DefaultLoginPageGeneratingFilter.class::cast)
									.findAny();
					if (optionalFilter.isPresent()) {
						UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = optionalFilter.get();
						Operation operation = new Operation();
						Schema<?> schema = new ObjectSchema()
								.addProperty(usernamePasswordAuthenticationFilter.getUsernameParameter(), new StringSchema())
								.addProperty(usernamePasswordAuthenticationFilter.getPasswordParameter(), new StringSchema());
						String mediaType = org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
						if (optionalDefaultLoginPageGeneratingFilter.isPresent()) {
							DefaultLoginPageGeneratingFilter defaultLoginPageGeneratingFilter = optionalDefaultLoginPageGeneratingFilter.get();
							try {
								boolean formLoginEnabled = (boolean) FieldUtils.readDeclaredField(defaultLoginPageGeneratingFilter, "formLoginEnabled", true);
								if (formLoginEnabled)
									mediaType = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
							}
							catch (IllegalAccessException e) {
								LOGGER.warn(e.getMessage());
							}
						}
						RequestBody requestBody = new RequestBody().content(new Content().addMediaType(mediaType, new MediaType().schema(schema)));
						operation.requestBody(requestBody);
						ApiResponses apiResponses = new ApiResponses();
						apiResponses.addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse().description(HttpStatus.OK.getReasonPhrase()));
						apiResponses.addApiResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()), new ApiResponse().description(HttpStatus.UNAUTHORIZED.getReasonPhrase()));
						operation.responses(apiResponses);
						operation.addTagsItem("login-endpoint");
						PathItem pathItem = new PathItem().post(operation);
						try {
							RequestMatcher requestMatcher = (RequestMatcher) FieldUtils.readField(
									usernamePasswordAuthenticationFilter,
									"requiresAuthenticationRequestMatcher",
									true
							);

							String loginPath = null;

							if (requestMatcher instanceof AntPathRequestMatcher) {
								loginPath = ((AntPathRequestMatcher) requestMatcher).getPattern();
							} else if (requestMatcher instanceof PathPatternRequestMatcher) {
								PathPattern pathPattern = (PathPattern) FieldUtils.readField(
										requestMatcher,
										"pattern",
										true
								);
								loginPath = pathPattern.getPatternString();
							}
							
							openAPI.getPaths().addPathItem(loginPath, pathItem);
						}
						catch (IllegalAccessException |
							   ClassCastException ignored) {
							// Exception escaped
							LOGGER.trace(ignored.getMessage());
						}
					}
				}
			};
		}
	}

	/**
	 * The type Spring doc security o auth 2 configuration.
	 */
	@Lazy(false)
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(OAuth2AuthorizationService.class)
	class SpringDocSecurityOAuth2Configuration {

		/**
		 * Spring security OAuth2 endpoint OpenAPI customizer.
		 *
		 * @return the open api customizer
		 */
		@Bean
		@ConditionalOnProperty(SPRINGDOC_SHOW_OAUTH2_ENDPOINTS)
		@Lazy(false)
		GlobalOpenApiCustomizer springDocSecurityOAuth2Customizer() {
			return new SpringDocSecurityOAuth2Customizer();
		}
	}

	/**
	 * The type Spring doc security o auth 2 client configuration.
	 */
	@Lazy(false)
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(RegisteredOAuth2AuthorizedClient.class)
	class SpringDocSecurityOAuth2ClientConfiguration {

		static {
			getConfig()
					.addAnnotationsToIgnore(RegisteredOAuth2AuthorizedClient.class);
		}

	}
}