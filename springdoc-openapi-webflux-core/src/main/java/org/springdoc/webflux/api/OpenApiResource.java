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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.PathUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.GenericResponseBuilder;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.webflux.core.visitor.RouterFunctionVisitor;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL_YAML;
import static org.springdoc.core.Constants.DEFAULT_GROUP_NAME;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Open api resource.
 * @author bnasslahsen
 */
@RestController
public class OpenApiResource extends AbstractOpenApiResource {

	/**
	 * The Request mapping handler mapping.
	 */
	private final RequestMappingInfoHandlerMapping requestMappingHandlerMapping;

	/**
	 * Instantiates a new Open api resource.
	 *
	 * @param groupName the group name
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param requestMappingHandlerMapping the request mapping handler mapping
	 * @param operationCustomizers the operation customizers
	 * @param openApiCustomisers the open api customisers
	 * @param springDocConfigProperties the spring doc config properties
	 * @param actuatorProvider the actuator provider
	 */
	public OpenApiResource(String groupName, ObjectFactory<OpenAPIBuilder> openAPIBuilderObjectFactory, AbstractRequestBuilder requestBuilder,
			GenericResponseBuilder responseBuilder, OperationBuilder operationParser,
			RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
			Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<OpenApiCustomiser>> openApiCustomisers,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<ActuatorProvider> actuatorProvider) {
		super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, operationCustomizers, openApiCustomisers, springDocConfigProperties, actuatorProvider);
		this.requestMappingHandlerMapping = requestMappingHandlerMapping;
	}

	/**
	 * Instantiates a new Open api resource.
	 *
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param requestMappingHandlerMapping the request mapping handler mapping
	 * @param operationCustomizers the operation customizers
	 * @param openApiCustomisers the open api customisers
	 * @param springDocConfigProperties the spring doc config properties
	 * @param actuatorProvider the actuator provider
	 */
	@Autowired
	public OpenApiResource(ObjectFactory<OpenAPIBuilder> openAPIBuilderObjectFactory, AbstractRequestBuilder requestBuilder,
			GenericResponseBuilder responseBuilder, OperationBuilder operationParser,
			RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
			Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<OpenApiCustomiser>> openApiCustomisers,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<ActuatorProvider> actuatorProvider) {
		super(DEFAULT_GROUP_NAME, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, operationCustomizers, openApiCustomisers, springDocConfigProperties, actuatorProvider);
		this.requestMappingHandlerMapping = requestMappingHandlerMapping;
	}

	/**
	 * Openapi json mono.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 * @return the mono
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<String> openapiJson(ServerHttpRequest serverHttpRequest, @Value(API_DOCS_URL) String apiDocsUrl)
			throws JsonProcessingException {
		calculateServerUrl(serverHttpRequest, apiDocsUrl);
		OpenAPI openAPI = this.getOpenApi();
		if (!springDocConfigProperties.isWriterWithDefaultPrettyPrinter())
			return Mono.just(Json.mapper().writeValueAsString(openAPI));
		else
			return Mono.just(Json.mapper().writerWithDefaultPrettyPrinter().writeValueAsString(openAPI));
	}

	/**
	 * Openapi yaml mono.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 * @return the mono
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value = DEFAULT_API_DOCS_URL_YAML, produces = APPLICATION_OPENAPI_YAML)
	public Mono<String> openapiYaml(ServerHttpRequest serverHttpRequest,
			@Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl) throws JsonProcessingException {
		calculateServerUrl(serverHttpRequest, apiDocsUrl);
		OpenAPI openAPI = this.getOpenApi();
		if (!springDocConfigProperties.isWriterWithDefaultPrettyPrinter())
			return Mono.just(getYamlMapper().writeValueAsString(openAPI));
		else
			return Mono.just(getYamlMapper().writerWithDefaultPrettyPrinter().writeValueAsString(openAPI));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void getPaths(Map<String, Object> restControllers) {
		Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
		calculatePath(restControllers, map);
		if (actuatorProvider.isPresent()) {
			map = actuatorProvider.get().getMethods();
			this.openAPIBuilder.addTag(new HashSet<>(map.values()), actuatorProvider.get().getTag());
			calculatePath(restControllers, map);
		}
		getWebFluxRouterFunctionPaths();
	}

	/**
	 * Calculate path.
	 *
	 * @param restControllers the rest controllers
	 * @param map the map
	 */
	protected void calculatePath(Map<String, Object> restControllers, Map<RequestMappingInfo, HandlerMethod> map) {
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
			RequestMappingInfo requestMappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();
			PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
			Set<PathPattern> patterns = patternsRequestCondition.getPatterns();
			for (PathPattern pathPattern : patterns) {
				String operationPath = pathPattern.getPatternString();
				Map<String, String> regexMap = new LinkedHashMap<>();
				operationPath = PathUtils.parsePath(operationPath, regexMap);
				String[] produces =  requestMappingInfo.getProducesCondition().getProducibleMediaTypes().stream().map(mediaType -> mediaType.toString()).toArray(String[]::new);
				String[] consumes =  requestMappingInfo.getConsumesCondition().getConsumableMediaTypes().stream().map(MimeType::toString).toArray(String[]::new);
				String[] headers =  requestMappingInfo.getHeadersCondition().getExpressions().stream().map(stringNameValueExpression -> stringNameValueExpression.toString()).toArray(String[]::new);
				if (operationPath.startsWith(DEFAULT_PATH_SEPARATOR)
						&& (restControllers.containsKey(handlerMethod.getBean().toString()) || actuatorProvider.isPresent())
						&& isFilterCondition(handlerMethod, operationPath, produces, consumes, headers)) {
					Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
					// default allowed requestmethods
					if (requestMethods.isEmpty())
						requestMethods = this.getDefaultAllowedHttpMethods();
					calculatePath(handlerMethod, operationPath, requestMethods);
				}
			}
		}
	}

	/**
	 * Gets web flux router function paths.
	 */
	protected void getWebFluxRouterFunctionPaths() {
		ApplicationContext applicationContext = requestMappingHandlerMapping.getApplicationContext();
		Map<String, RouterFunction> routerBeans = Objects.requireNonNull(applicationContext).getBeansOfType(RouterFunction.class);
		for (Map.Entry<String, RouterFunction> entry : routerBeans.entrySet()) {
			RouterFunction routerFunction = entry.getValue();
			RouterFunctionVisitor routerFunctionVisitor = new RouterFunctionVisitor();
			routerFunction.accept(routerFunctionVisitor);
			getRouterFunctionPaths(entry.getKey(), routerFunctionVisitor);
		}
	}

	/**
	 * Calculate server url.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 */
	protected void calculateServerUrl(ServerHttpRequest serverHttpRequest, String apiDocsUrl) {
		super.initOpenAPIBuilder();
		String requestUrl = decode(serverHttpRequest.getURI().toString());
		String serverBaseUrl = requestUrl.substring(0, requestUrl.length() - apiDocsUrl.length());
		openAPIBuilder.setServerBaseUrl(serverBaseUrl);
	}

}
