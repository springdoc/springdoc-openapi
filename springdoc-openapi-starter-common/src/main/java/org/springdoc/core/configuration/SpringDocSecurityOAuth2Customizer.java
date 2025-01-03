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

import java.lang.reflect.Field;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.configuration.oauth2.SpringDocOAuth2AuthorizationServerMetadata;
import org.springdoc.core.configuration.oauth2.SpringDocOAuth2Token;
import org.springdoc.core.configuration.oauth2.SpringDocOAuth2TokenIntrospection;
import org.springdoc.core.configuration.oauth2.SpringDocOidcClientRegistrationRequest;
import org.springdoc.core.configuration.oauth2.SpringDocOidcClientRegistrationResponse;
import org.springdoc.core.configuration.oauth2.SpringDocOidcProviderConfiguration;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.oidc.web.OidcClientRegistrationEndpointFilter;
import org.springframework.security.oauth2.server.authorization.oidc.web.OidcProviderConfigurationEndpointFilter;
import org.springframework.security.oauth2.server.authorization.oidc.web.OidcUserInfoEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.NimbusJwkSetEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationServerMetadataEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenIntrospectionEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenRevocationEndpointFilter;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.ReflectionUtils;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * The type Spring doc security o auth 2 customizer.
 *
 * @author bnasslahsen
 */
public class SpringDocSecurityOAuth2Customizer implements GlobalOpenApiCustomizer, ApplicationContextAware {

	/**
	 * The constant REQUEST_MATCHER.
	 */
	private static final String REQUEST_MATCHER = "requestMatcher";

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDocSecurityOAuth2Customizer.class);

	/**
	 * The constant OAUTH2_ENDPOINT_TAG.
	 */
	private static final String OAUTH2_ENDPOINT_TAG = "authorization-server-endpoints";

	/**
	 * The Context.
	 */
	private ApplicationContext applicationContext;

	@Override
	public void customise(OpenAPI openAPI) {
		FilterChainProxy filterChainProxy = applicationContext.getBean(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, FilterChainProxy.class);
		boolean openapi31 = SpecVersion.V31 == openAPI.getSpecVersion();
		for (SecurityFilterChain filterChain : filterChainProxy.getFilterChains()) {
			getNimbusJwkSetEndpoint(openAPI, filterChain, openapi31);
			getOAuth2AuthorizationServerMetadataEndpoint(openAPI, filterChain, openapi31);
			getOAuth2TokenEndpoint(openAPI, filterChain, openapi31);
			getOAuth2AuthorizationEndpoint(openAPI, filterChain, openapi31);
			getOAuth2TokenIntrospectionEndpointFilter(openAPI, filterChain, openapi31);
			getOAuth2TokenRevocationEndpointFilter(openAPI, filterChain, openapi31);
			getOidcProviderConfigurationEndpoint(openAPI, filterChain, openapi31);
			getOidcUserInfoEndpoint(openAPI, filterChain);
			getOidcClientRegistrationEndpoint(openAPI, filterChain, openapi31);
		}
	}

	/**
	 * Gets o auth 2 token revocation endpoint filter.
	 *
	 * @param openAPI             the open api
	 * @param securityFilterChain the security filter chain
	 * @param openapi31           the openapi 31
	 */
	private void getOAuth2TokenRevocationEndpointFilter(OpenAPI openAPI, SecurityFilterChain securityFilterChain, boolean openapi31) {
		Object oAuth2EndpointFilter =
				new SpringDocSecurityOAuth2EndpointUtils(OAuth2TokenRevocationEndpointFilter.class).findEndpoint(securityFilterChain);
		if (oAuth2EndpointFilter != null) {
			ApiResponses apiResponses = new ApiResponses();
			apiResponses.addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse().description(HttpStatus.OK.getReasonPhrase()));
			buildApiResponsesOnInternalServerError(apiResponses);
			buildApiResponsesOnBadRequest(apiResponses, openAPI, openapi31);

			Operation operation = buildOperation(apiResponses);
			Schema<?> schema = new ObjectSchema()
					.addProperty("token", new StringSchema())
					.addProperty(OAuth2ParameterNames.TOKEN_TYPE_HINT, new StringSchema());

			String mediaType = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
			RequestBody requestBody = new RequestBody().content(new Content().addMediaType(mediaType, new MediaType().schema(schema)));
			operation.setRequestBody(requestBody);
			buildPath(oAuth2EndpointFilter, "tokenRevocationEndpointMatcher", openAPI, operation, HttpMethod.POST);
		}
	}

	/**
	 * Gets o auth 2 token introspection endpoint filter.
	 *
	 * @param openAPI             the open api
	 * @param securityFilterChain the security filter chain
	 * @param openapi31           the openapi 31
	 */
	private void getOAuth2TokenIntrospectionEndpointFilter(OpenAPI openAPI, SecurityFilterChain securityFilterChain, boolean openapi31) {
		Object oAuth2EndpointFilter =
				new SpringDocSecurityOAuth2EndpointUtils(OAuth2TokenIntrospectionEndpointFilter.class).findEndpoint(securityFilterChain);
		if (oAuth2EndpointFilter != null) {
			ApiResponses apiResponses = new ApiResponses();
			buildApiResponsesOnSuccess(apiResponses, AnnotationsUtils.resolveSchemaFromType(SpringDocOAuth2TokenIntrospection.class, openAPI.getComponents(), null, openapi31));
			buildApiResponsesOnInternalServerError(apiResponses);
			buildApiResponsesOnBadRequest(apiResponses, openAPI, openapi31);

			Operation operation = buildOperation(apiResponses);
			Schema<?> requestSchema = new ObjectSchema()
					.addProperty("token", new StringSchema())
					.addProperty(OAuth2ParameterNames.TOKEN_TYPE_HINT, new StringSchema())
					.addProperty("additionalParameters", new ObjectSchema().additionalProperties(new StringSchema()));

			String mediaType = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
			RequestBody requestBody = new RequestBody().content(new Content().addMediaType(mediaType, new MediaType().schema(requestSchema)));
			operation.setRequestBody(requestBody);
			buildPath(oAuth2EndpointFilter, "tokenIntrospectionEndpointMatcher", openAPI, operation, HttpMethod.POST);
		}
	}

	/**
	 * Gets o auth 2 authorization server metadata endpoint.
	 *
	 * @param openAPI             the open api
	 * @param securityFilterChain the security filter chain
	 * @param openapi31           the openapi 31
	 */
	private void getOAuth2AuthorizationServerMetadataEndpoint(OpenAPI openAPI, SecurityFilterChain securityFilterChain, boolean openapi31) {
		Class<OAuth2AuthorizationServerMetadataEndpointFilter>authorizationServerMetadataEndpointClass = OAuth2AuthorizationServerMetadataEndpointFilter.class;
		Object oAuth2EndpointFilter =
				new SpringDocSecurityOAuth2EndpointUtils(authorizationServerMetadataEndpointClass).findEndpoint(securityFilterChain);
		if (oAuth2EndpointFilter != null) {
			ApiResponses apiResponses = new ApiResponses();
			buildApiResponsesOnSuccess(apiResponses, AnnotationsUtils.resolveSchemaFromType(SpringDocOAuth2AuthorizationServerMetadata.class, openAPI.getComponents(), null, openapi31));
			buildApiResponsesOnInternalServerError(apiResponses);
			Operation operation = buildOperation(apiResponses);
			Field field = ReflectionUtils.findField(authorizationServerMetadataEndpointClass, "DEFAULT_OAUTH2_AUTHORIZATION_SERVER_METADATA_ENDPOINT_URI");
			if (field != null) {
				ReflectionUtils.makeAccessible(field);
				String defaultOauth2MetadataUri = (String) ReflectionUtils.getField(field, null);
				openAPI.getPaths().addPathItem(defaultOauth2MetadataUri , new PathItem().get(operation));
				operation = buildOperation(apiResponses);
				operation.addParametersItem(new PathParameter().name("subpath").schema(new StringSchema()));
				operation.summary("Valid when multiple issuers are allowed");
				openAPI.getPaths().addPathItem(defaultOauth2MetadataUri+"/{subpath}" , new PathItem().get(operation));
			}
		}
	}

	/**
	 * Gets nimbus jwk set endpoint.
	 *
	 * @param openAPI             the open api
	 * @param securityFilterChain the security filter chain
	 * @param openapi31           the openapi 31
	 */
	private void getNimbusJwkSetEndpoint(OpenAPI openAPI, SecurityFilterChain securityFilterChain, boolean openapi31) {
		Object oAuth2EndpointFilter =
				new SpringDocSecurityOAuth2EndpointUtils(NimbusJwkSetEndpointFilter.class).findEndpoint(securityFilterChain);
		if (oAuth2EndpointFilter != null) {
			ApiResponses apiResponses = new ApiResponses();
			Schema<?> schema = new MapSchema();
			schema.addProperty("keys", new ArraySchema().items(new ObjectSchema().additionalProperties(true)));

			ApiResponse response = new ApiResponse().description(HttpStatus.OK.getReasonPhrase()).content(new Content().addMediaType(
					APPLICATION_JSON_VALUE,
					new MediaType().schema(schema)));
			apiResponses.addApiResponse(String.valueOf(HttpStatus.OK.value()), response);
			buildApiResponsesOnInternalServerError(apiResponses);
			buildApiResponsesOnBadRequest(apiResponses, openAPI, openapi31);

			Operation operation = buildOperation(apiResponses);
			operation.responses(apiResponses);
			buildPath(oAuth2EndpointFilter, REQUEST_MATCHER, openAPI, operation, HttpMethod.GET);
		}
	}

	/**
	 * Gets o auth 2 token endpoint.
	 *
	 * @param openAPI             the open api
	 * @param securityFilterChain the security filter chain
	 * @param openapi31           the openapi 31
	 */
	private void getOAuth2TokenEndpoint(OpenAPI openAPI, SecurityFilterChain securityFilterChain, boolean openapi31) {
		Object oAuth2EndpointFilter =
				new SpringDocSecurityOAuth2EndpointUtils(OAuth2TokenEndpointFilter.class).findEndpoint(securityFilterChain);

		if (oAuth2EndpointFilter != null) {
			ApiResponses apiResponses = new ApiResponses();
			buildApiResponsesOnSuccess(apiResponses, AnnotationsUtils.resolveSchemaFromType(SpringDocOAuth2Token.class, openAPI.getComponents(), null, openapi31));
			buildApiResponsesOnInternalServerError(apiResponses);
			buildApiResponsesOnBadRequest(apiResponses, openAPI, openapi31);
			buildOAuth2Error(openAPI, apiResponses, HttpStatus.UNAUTHORIZED, openapi31);
			Operation operation = buildOperation(apiResponses);

			Schema<?> requestSchema = new ObjectSchema()
					.addProperty(OAuth2ParameterNames.GRANT_TYPE,
							new StringSchema()
									.addEnumItem(AuthorizationGrantType.AUTHORIZATION_CODE.getValue())
									.addEnumItem(AuthorizationGrantType.REFRESH_TOKEN.getValue())
									.addEnumItem(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()))
					.addProperty(OAuth2ParameterNames.CODE, new StringSchema())
					.addProperty(OAuth2ParameterNames.REDIRECT_URI, new StringSchema())
					.addProperty(OAuth2ParameterNames.REFRESH_TOKEN, new StringSchema())
					.addProperty(OAuth2ParameterNames.SCOPE, new StringSchema())
					.addProperty(OAuth2ParameterNames.CLIENT_ID, new StringSchema())
					.addProperty(OAuth2ParameterNames.CLIENT_SECRET, new StringSchema())
					.addProperty(OAuth2ParameterNames.CLIENT_ASSERTION_TYPE, new StringSchema())
					.addProperty(OAuth2ParameterNames.CLIENT_ASSERTION, new StringSchema())
					.addProperty("additionalParameters", new ObjectSchema().additionalProperties(new StringSchema()));

			String mediaType = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
			RequestBody requestBody = new RequestBody().content(new Content().addMediaType(mediaType, new MediaType().schema(requestSchema)));
			operation.setRequestBody(requestBody);
			operation.addParametersItem(new HeaderParameter().name("Authorization").schema(new StringSchema()));

			buildPath(oAuth2EndpointFilter, "tokenEndpointMatcher", openAPI, operation, HttpMethod.POST);
		}
	}

	/**
	 * Gets o auth 2 authorization endpoint.
	 *
	 * @param openAPI             the open api
	 * @param securityFilterChain the security filter chain
	 * @param openapi31           the openapi 31
	 */
	private void getOAuth2AuthorizationEndpoint(OpenAPI openAPI, SecurityFilterChain securityFilterChain, boolean openapi31) {
		Object oAuth2EndpointFilter =
				new SpringDocSecurityOAuth2EndpointUtils(OAuth2AuthorizationEndpointFilter.class).findEndpoint(securityFilterChain);
		if (oAuth2EndpointFilter != null) {
			ApiResponses apiResponses = new ApiResponses();

			ApiResponse response = new ApiResponse().description(HttpStatus.OK.getReasonPhrase()).content(new Content().addMediaType(
					TEXT_HTML_VALUE,
					new MediaType()));
			apiResponses.addApiResponse(String.valueOf(HttpStatus.OK.value()), response);
			buildApiResponsesOnInternalServerError(apiResponses);
			buildApiResponsesOnBadRequest(apiResponses, openAPI, openapi31);
			apiResponses.addApiResponse(String.valueOf(HttpStatus.FOUND.value()),
					new ApiResponse().description(HttpStatus.FOUND.getReasonPhrase())
							.addHeaderObject("Location", new Header().schema(new StringSchema())));
			Operation operation = buildOperation(apiResponses);
			Schema<?> schema = new ObjectSchema().additionalProperties(new StringSchema());
			operation.addParametersItem(new Parameter().name("parameters").in(ParameterIn.QUERY.toString()).schema(schema));
			buildPath(oAuth2EndpointFilter, "authorizationEndpointMatcher", openAPI, operation, HttpMethod.POST);
		}
	}

	/**
	 * Gets OpenID Provider endpoint filter
	 *
	 * @param openAPI             the open api
	 * @param securityFilterChain the security filter chain
	 * @param openapi31           the openapi 31
	 */
	private void getOidcProviderConfigurationEndpoint(OpenAPI openAPI, SecurityFilterChain securityFilterChain, boolean openapi31) {
		Class<OidcProviderConfigurationEndpointFilter> oidcProviderConfigurationEndpointFilterClass = OidcProviderConfigurationEndpointFilter.class;
		Object oAuth2EndpointFilter =
				new SpringDocSecurityOAuth2EndpointUtils(oidcProviderConfigurationEndpointFilterClass).findEndpoint(securityFilterChain);

		if (oAuth2EndpointFilter != null) {
			ApiResponses apiResponses = new ApiResponses();
			buildApiResponsesOnSuccess(apiResponses, AnnotationsUtils.resolveSchemaFromType(SpringDocOidcProviderConfiguration.class, openAPI.getComponents(), null, openapi31));
			buildApiResponsesOnInternalServerError(apiResponses);
			Operation operation = buildOperation(apiResponses);

			Field field = ReflectionUtils.findField(oidcProviderConfigurationEndpointFilterClass, "DEFAULT_OIDC_PROVIDER_CONFIGURATION_ENDPOINT_URI");
			if (field != null) {
				ReflectionUtils.makeAccessible(field);
				String defaultOidcConfigUri = (String) ReflectionUtils.getField(field, null);
				openAPI.getPaths().addPathItem(defaultOidcConfigUri , new PathItem().get(operation));
				operation = buildOperation(apiResponses);
				operation.addParametersItem(new PathParameter().name("subpath").schema(new StringSchema()));
				operation.summary("Valid when multiple issuers are allowed");
				openAPI.getPaths().addPathItem("/{subpath}"+defaultOidcConfigUri , new PathItem().get(operation));
			}
		}
	}

	/**
	 * Gets OpenID UserInfo endpoint filter
	 *
	 * @param openAPI             the open api
	 * @param securityFilterChain the security filter chain
	 */
	private void getOidcUserInfoEndpoint(OpenAPI openAPI, SecurityFilterChain securityFilterChain) {
		Object oAuth2EndpointFilter =
				new SpringDocSecurityOAuth2EndpointUtils(OidcUserInfoEndpointFilter.class).findEndpoint(securityFilterChain);

		if (oAuth2EndpointFilter != null) {
			ApiResponses apiResponses = new ApiResponses();
			Schema<?> schema = new ObjectSchema().additionalProperties(new StringSchema());
			buildApiResponsesOnSuccess(apiResponses, schema);
			buildApiResponsesOnInternalServerError(apiResponses);
			Operation operation = buildOperation(apiResponses);
			buildPath(oAuth2EndpointFilter, "userInfoEndpointMatcher", openAPI, operation, HttpMethod.GET);
		}
	}

	/**
	 * Gets OpenID Client Registration endpoint filter
	 *
	 * @param openAPI             the open api
	 * @param securityFilterChain the security filter chain
	 * @param openapi31           the openapi 31
	 */
	private void getOidcClientRegistrationEndpoint(OpenAPI openAPI, SecurityFilterChain securityFilterChain, boolean openapi31) {
		Object oAuth2EndpointFilter =
				new SpringDocSecurityOAuth2EndpointUtils(OidcClientRegistrationEndpointFilter.class).findEndpoint(securityFilterChain);

		if (oAuth2EndpointFilter != null) {
			ApiResponses apiResponses = new ApiResponses();
			buildApiResponsesOnCreated(apiResponses, AnnotationsUtils.resolveSchemaFromType(SpringDocOidcClientRegistrationResponse.class, openAPI.getComponents(), null,openapi31 ));
			buildApiResponsesOnInternalServerError(apiResponses);
			buildApiResponsesOnBadRequest(apiResponses, openAPI, openapi31);
			buildOAuth2Error(openAPI, apiResponses, HttpStatus.UNAUTHORIZED, openapi31);
			buildOAuth2Error(openAPI, apiResponses, HttpStatus.FORBIDDEN, openapi31);
			Operation operation = buildOperation(apiResponses);

			// OidcClientRegistration
			Schema schema = AnnotationsUtils.resolveSchemaFromType(SpringDocOidcClientRegistrationRequest.class, openAPI.getComponents(), null, openapi31);

			String mediaType = APPLICATION_JSON_VALUE;
			RequestBody requestBody = new RequestBody().content(new Content().addMediaType(mediaType, new MediaType().schema(schema)));
			operation.setRequestBody(requestBody);
			operation.addParametersItem(new HeaderParameter().name("Authorization").schema(new StringSchema()));

			buildPath(oAuth2EndpointFilter, "clientRegistrationEndpointMatcher", openAPI, operation, HttpMethod.POST);
		}
	}

	/**
	 * Build operation.
	 *
	 * @param apiResponses the api responses
	 * @return the operation
	 */
	private Operation buildOperation(ApiResponses apiResponses) {
		Operation operation = new Operation();
		operation.addTagsItem(OAUTH2_ENDPOINT_TAG);
		operation.responses(apiResponses);
		return operation;
	}

	/**
	 * Build api responses api responses on success.
	 *
	 * @param apiResponses the api responses
	 * @param schema       the schema
	 * @return the api responses
	 */
	private ApiResponses buildApiResponsesOnSuccess(ApiResponses apiResponses, Schema schema) {
		ApiResponse response = new ApiResponse().description(HttpStatus.OK.getReasonPhrase()).content(new Content().addMediaType(
				APPLICATION_JSON_VALUE,
				new MediaType().schema(schema)));
		apiResponses.addApiResponse(String.valueOf(HttpStatus.OK.value()), response);
		return apiResponses;
	}

	/**
	 * Build api responses api responses on created.
	 *
	 * @param apiResponses the api responses
	 * @param schema       the schema
	 * @return the api responses
	 */
	private ApiResponses buildApiResponsesOnCreated(ApiResponses apiResponses, Schema schema) {
		ApiResponse response = new ApiResponse().description(HttpStatus.CREATED.getReasonPhrase()).content(new Content().addMediaType(
				APPLICATION_JSON_VALUE,
				new MediaType().schema(schema)));
		apiResponses.addApiResponse(String.valueOf(HttpStatus.CREATED.value()), response);
		return apiResponses;
	}

	/**
	 * Build api responses api responses on internal server error.
	 *
	 * @param apiResponses the api responses
	 * @return the api responses
	 */
	private ApiResponses buildApiResponsesOnInternalServerError(ApiResponses apiResponses) {
		apiResponses.addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), new ApiResponse().description(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
		return apiResponses;
	}

	/**
	 * Build api responses on bad request.
	 *
	 * @param apiResponses the api responses
	 * @param openAPI      the open api
	 * @param openapi31    the openapi 31
	 * @return the api responses
	 */
	private ApiResponses buildApiResponsesOnBadRequest(ApiResponses apiResponses, OpenAPI openAPI, boolean openapi31) {
		buildOAuth2Error(openAPI, apiResponses, HttpStatus.BAD_REQUEST, openapi31);
		return apiResponses;
	}

	/**
	 * Build o auth 2 error.
	 *
	 * @param openAPI      the open api
	 * @param apiResponses the api responses
	 * @param httpStatus   the http status
	 * @param openapi31    the openapi 31
	 */
	private static void buildOAuth2Error(OpenAPI openAPI, ApiResponses apiResponses, HttpStatus httpStatus, boolean openapi31) {
		Schema oAuth2ErrorSchema = AnnotationsUtils.resolveSchemaFromType(OAuth2Error.class, openAPI.getComponents(), null, openapi31);
		apiResponses.addApiResponse(String.valueOf(httpStatus.value()), new ApiResponse().description(httpStatus.getReasonPhrase()).content(new Content().addMediaType(
				APPLICATION_JSON_VALUE,
				new MediaType().schema(oAuth2ErrorSchema))));
	}

	/**
	 * Build path.
	 *
	 * @param oAuth2EndpointFilter         the o auth 2 endpoint filter
	 * @param authorizationEndpointMatcher the authorization endpoint matcher
	 * @param openAPI                      the open api
	 * @param operation                    the operation
	 * @param requestMethod                the request method
	 */
	private void buildPath(Object oAuth2EndpointFilter, String authorizationEndpointMatcher, OpenAPI openAPI, Operation operation, HttpMethod requestMethod) {
		try {
			RequestMatcher endpointMatcher = (RequestMatcher) FieldUtils.readDeclaredField(oAuth2EndpointFilter, authorizationEndpointMatcher, true);
			String path = null;
			if (endpointMatcher instanceof AntPathRequestMatcher antPathRequestMatcher)
				path = antPathRequestMatcher.getPattern();
			else if (endpointMatcher instanceof OrRequestMatcher endpointMatchers) {
				Iterable<RequestMatcher> requestMatchers = (Iterable<RequestMatcher>) FieldUtils.readDeclaredField(endpointMatchers, "requestMatchers", true);
				for (RequestMatcher requestMatcher : requestMatchers) {
					if (requestMatcher instanceof OrRequestMatcher orRequestMatcher) {
						requestMatchers = (Iterable<RequestMatcher>) FieldUtils.readDeclaredField(orRequestMatcher, "requestMatchers", true);
						for (RequestMatcher matcher : requestMatchers) {
							if (matcher instanceof AntPathRequestMatcher antPathRequestMatcher)
								path = antPathRequestMatcher.getPattern();
						}
					}
					else if (requestMatcher instanceof AntPathRequestMatcher antPathRequestMatcher) {
						path = antPathRequestMatcher.getPattern();
					}
				}
			}

			PathItem pathItem = new PathItem();
			if (HttpMethod.POST.equals(requestMethod)) {
				pathItem.post(operation);
			}
			else if (HttpMethod.GET.equals(requestMethod)) {
				pathItem.get(operation);
			}
			openAPI.getPaths().addPathItem(path, pathItem);
		}
		catch (IllegalAccessException | ClassCastException ignored) {
			LOGGER.trace(ignored.getMessage());
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
