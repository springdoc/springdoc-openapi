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

package org.springdoc.webflux.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.GenericResponseBuilder;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfigProperties.GroupConfig;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL_YAML;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Multiple open api resource.
 * @author bnasslahsen
 */
@RestController
public class MultipleOpenApiResource implements InitializingBean {

	/**
	 * The Grouped open apis.
	 */
	private final List<GroupedOpenApi> groupedOpenApis;

	/**
	 * The Default open api builder.
	 */
	private final ObjectFactory<OpenAPIBuilder> defaultOpenAPIBuilder;

	/**
	 * The Request builder.
	 */
	private final AbstractRequestBuilder requestBuilder;

	/**
	 * The Response builder.
	 */
	private final GenericResponseBuilder responseBuilder;

	/**
	 * The Operation parser.
	 */
	private final OperationBuilder operationParser;

	/**
	 * The Request mapping handler mapping.
	 */
	private final RequestMappingInfoHandlerMapping requestMappingHandlerMapping;

	/**
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The Grouped open api resources.
	 */
	private Map<String, OpenApiResource> groupedOpenApiResources;

	/**
	 * The Actuator provider.
	 */
	private Optional<ActuatorProvider> actuatorProvider;

	/**
	 * Instantiates a new Multiple open api resource.
	 *
	 * @param groupedOpenApis the grouped open apis
	 * @param defaultOpenAPIBuilder the default open api builder
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param requestMappingHandlerMapping the request mapping handler mapping
	 * @param springDocConfigProperties the spring doc config properties
	 * @param actuatorProvider the actuator provider
	 */
	public MultipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis,
			ObjectFactory<OpenAPIBuilder> defaultOpenAPIBuilder, AbstractRequestBuilder requestBuilder,
			GenericResponseBuilder responseBuilder, OperationBuilder operationParser,
			RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
			SpringDocConfigProperties springDocConfigProperties, Optional<ActuatorProvider> actuatorProvider) {

		this.groupedOpenApis = groupedOpenApis;
		this.defaultOpenAPIBuilder = defaultOpenAPIBuilder;
		this.requestBuilder = requestBuilder;
		this.responseBuilder = responseBuilder;
		this.operationParser = operationParser;
		this.requestMappingHandlerMapping = requestMappingHandlerMapping;
		this.springDocConfigProperties = springDocConfigProperties;
		this.actuatorProvider = actuatorProvider;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.groupedOpenApiResources = groupedOpenApis.stream()
				.collect(Collectors.toMap(GroupedOpenApi::getGroup, item ->
						{
							GroupConfig groupConfig = new GroupConfig(item.getGroup(), item.getPathsToMatch(), item.getPackagesToScan(), item.getPackagesToExclude(), item.getPathsToExclude(), item.getProducesToMatch(), item.getConsumesToMatch(),item.getHeadersToMatch());
							springDocConfigProperties.addGroupConfig(groupConfig);
							return new OpenApiResource(item.getGroup(),
									defaultOpenAPIBuilder,
									requestBuilder,
									responseBuilder,
									operationParser,
									requestMappingHandlerMapping,
									Optional.of(item.getOperationCustomizers()),
									Optional.of(item.getOpenApiCustomisers()),
									springDocConfigProperties,
									actuatorProvider
							);
						}
				));
	}

	/**
	 * Openapi json mono.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 * @param group the group
	 * @return the mono
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value = API_DOCS_URL + "/{group}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<String> openapiJson(ServerHttpRequest
			serverHttpRequest, @Value(API_DOCS_URL) String apiDocsUrl, @PathVariable String
			group)
			throws JsonProcessingException {
		return getOpenApiResourceOrThrow(group).openapiJson(serverHttpRequest, apiDocsUrl + DEFAULT_PATH_SEPARATOR + group);
	}

	/**
	 * Openapi yaml mono.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 * @param group the group
	 * @return the mono
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value = DEFAULT_API_DOCS_URL_YAML + "/{group}", produces = APPLICATION_OPENAPI_YAML)
	public Mono<String> openapiYaml(ServerHttpRequest serverHttpRequest,
			@Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl, @PathVariable String
			group) throws JsonProcessingException {
		return getOpenApiResourceOrThrow(group).openapiYaml(serverHttpRequest, apiDocsUrl + DEFAULT_PATH_SEPARATOR + group);
	}

	/**
	 * Gets open api resource or throw.
	 *
	 * @param group the group
	 * @return the open api resource or throw
	 */
	private OpenApiResource getOpenApiResourceOrThrow(String group) {
		OpenApiResource openApiResource = groupedOpenApiResources.get(group);
		if (openApiResource == null) {
			throw new OpenApiResourceNotFoundException("No OpenAPI resource found for group: " + group);
		}
		return openApiResource;
	}
}