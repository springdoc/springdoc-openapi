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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.PathUtils;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocProviders;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.filters.OpenApiMethodFilter;
import org.springdoc.core.providers.ActuatorProvider;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.webflux.core.visitor.RouterFunctionVisitor;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.result.method.RequestMappingInfo;

import static org.springdoc.core.Constants.DEFAULT_GROUP_NAME;
import static org.springdoc.core.providers.ActuatorProvider.getTag;

/**
 * The type Open api resource.
 * @author bnasslahsen, Azige
 */
public abstract class OpenApiResource extends AbstractOpenApiResource {

	/**
	 * Instantiates a new Open api resource.
	 *
	 * @param groupName the group name
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param operationCustomizers the operation customizers
	 * @param openApiCustomisers the open api customisers
	 * @param methodFilters the method filters
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springDocProviders the spring doc providers
	 */
	public OpenApiResource(String groupName, ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<OpenApiCustomiser>> openApiCustomisers,
			Optional<List<OpenApiMethodFilter>> methodFilters,
			SpringDocConfigProperties springDocConfigProperties,
			SpringDocProviders springDocProviders) {
		super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, operationCustomizers, openApiCustomisers, methodFilters, springDocConfigProperties, springDocProviders);
	}

	/**
	 * Instantiates a new Open api resource.
	 *
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param operationCustomizers the operation customizers
	 * @param openApiCustomisers the open api customisers
	 * @param methodFilters the method filters
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springDocProviders the spring doc providers
	 */
	public OpenApiResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<OpenApiCustomiser>> openApiCustomisers,
			Optional<List<OpenApiMethodFilter>> methodFilters,
			SpringDocConfigProperties springDocConfigProperties,
			SpringDocProviders springDocProviders) {
		super(DEFAULT_GROUP_NAME, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, operationCustomizers, openApiCustomisers, methodFilters, springDocConfigProperties, springDocProviders);
	}


	/**
	 * Openapi json mono.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 * @param locale the locale
	 * @return the mono
	 * @throws JsonProcessingException the json processing exception
	 */
	protected Mono<String> openapiJson(ServerHttpRequest serverHttpRequest, String apiDocsUrl, Locale locale)
			throws JsonProcessingException {
		calculateServerUrl(serverHttpRequest, apiDocsUrl, locale);
		OpenAPI openAPI = this.getOpenApi(locale);
		return Mono.just(writeJsonValue(openAPI));
	}

	/**
	 * Openapi yaml mono.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 * @param locale the locale
	 * @return the mono
	 * @throws JsonProcessingException the json processing exception
	 */
	protected Mono<String> openapiYaml(ServerHttpRequest serverHttpRequest, String apiDocsUrl, Locale locale)
			throws JsonProcessingException {
		calculateServerUrl(serverHttpRequest, apiDocsUrl, locale);
		OpenAPI openAPI = this.getOpenApi(locale);
		return Mono.just(writeYamlValue(openAPI));
	}

	/**
	 * Gets paths.
	 *
	 * @param restControllers the rest controllers
	 * @param locale the locale
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void getPaths(Map<String, Object> restControllers, Locale locale) {
		Optional<SpringWebProvider> springWebProviderOptional = springDocProviders.getSpringWebProvider();
		springWebProviderOptional.ifPresent(springWebProvider -> {
			Map<RequestMappingInfo, HandlerMethod> map = springWebProvider.getHandlerMethods();
			calculatePath(restControllers, map, locale);
			Optional<ActuatorProvider> actuatorProviderOptional = springDocProviders.getActuatorProvider();
			if (actuatorProviderOptional.isPresent() && springDocConfigProperties.isShowActuator()) {
				map = actuatorProviderOptional.get().getMethods();
				this.openAPIService.addTag(new HashSet<>(map.values()), getTag());
				calculatePath(restControllers, map, locale);
			}
			getWebFluxRouterFunctionPaths(locale);
		});
	}

	/**
	 * Calculate path.
	 *
	 * @param restControllers the rest controllers
	 * @param map the map
	 * @param locale the locale
	 */
	protected void calculatePath(Map<String, Object> restControllers, Map<RequestMappingInfo, HandlerMethod> map, Locale locale) {
		List<Map.Entry<RequestMappingInfo, HandlerMethod>> entries = new ArrayList<>(map.entrySet());
		entries.sort(byReversedRequestMappingInfos());
		Optional<SpringWebProvider> springWebProviderOptional = springDocProviders.getSpringWebProvider();
		springWebProviderOptional.ifPresent(springWebProvider -> {
			for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : entries) {
				RequestMappingInfo requestMappingInfo = entry.getKey();
				HandlerMethod handlerMethod = entry.getValue();
				Set<String> patterns = springWebProvider.getActivePatterns(requestMappingInfo);
				for (String operationPath : patterns) {
					Map<String, String> regexMap = new LinkedHashMap<>();
					operationPath = PathUtils.parsePath(operationPath, regexMap);
					String[] produces = requestMappingInfo.getProducesCondition().getProducibleMediaTypes().stream().map(MimeType::toString).toArray(String[]::new);
					String[] consumes = requestMappingInfo.getConsumesCondition().getConsumableMediaTypes().stream().map(MimeType::toString).toArray(String[]::new);
					String[] headers = requestMappingInfo.getHeadersCondition().getExpressions().stream().map(Object::toString).toArray(String[]::new);
					if ((isRestController(restControllers, handlerMethod, operationPath) || isActuatorRestController(operationPath, handlerMethod))
							&& isFilterCondition(handlerMethod, operationPath, produces, consumes, headers)) {
						Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
						// default allowed requestmethods
						if (requestMethods.isEmpty())
							requestMethods = this.getDefaultAllowedHttpMethods();
						calculatePath(handlerMethod, operationPath, requestMethods, locale);
					}
				}
			}
		});
	}

	/**
	 * By reversed request mapping infos comparator.
	 *
	 * @return the comparator
	 */
	private Comparator<Map.Entry<RequestMappingInfo, HandlerMethod>> byReversedRequestMappingInfos() {
		return Comparator.<Map.Entry<RequestMappingInfo, HandlerMethod>, String>
						comparing(a -> a.getKey().toString())
				.reversed();
	}

	/**
	 * Gets web flux router function paths.
	 * @param locale the locale
	 */
	protected void getWebFluxRouterFunctionPaths(Locale locale) {
		Map<String, RouterFunction> routerBeans = Objects.requireNonNull(openAPIService.getContext()).getBeansOfType(RouterFunction.class);
		for (Map.Entry<String, RouterFunction> entry : routerBeans.entrySet()) {
			RouterFunction routerFunction = entry.getValue();
			RouterFunctionVisitor routerFunctionVisitor = new RouterFunctionVisitor();
			routerFunction.accept(routerFunctionVisitor);
			getRouterFunctionPaths(entry.getKey(), routerFunctionVisitor, locale);
		}
	}


	/**
	 * Calculate server url.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 * @param locale the locale
	 */
	protected void calculateServerUrl(ServerHttpRequest serverHttpRequest, String apiDocsUrl, Locale locale) {
		initOpenAPIBuilder(locale);
		String serverUrl = getServerUrl(serverHttpRequest, apiDocsUrl);
		openAPIService.setServerBaseUrl(serverUrl);
	}

	/**
	 * Gets server url.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 * @return the server url
	 */
	protected abstract String getServerUrl(ServerHttpRequest serverHttpRequest, String apiDocsUrl);

}
