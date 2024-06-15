/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2022 the original author or authors.
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

package org.springdoc.webflux.api;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.PathUtils;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ActuatorProvider;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.webflux.core.visitor.RouterFunctionVisitor;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.result.method.RequestMappingInfo;

import static org.springdoc.core.providers.ActuatorProvider.getTag;
import static org.springdoc.core.utils.Constants.DEFAULT_GROUP_NAME;

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
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springDocProviders the spring doc providers
	 * @param springDocCustomizers the spring doc customizers
	 */
	public OpenApiResource(String groupName, ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			SpringDocConfigProperties springDocConfigProperties,
			SpringDocProviders springDocProviders, SpringDocCustomizers springDocCustomizers) {
		super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser,springDocConfigProperties, springDocProviders, springDocCustomizers);
	}

	/**
	 * Instantiates a new Open api resource.
	 *
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springDocProviders the spring doc providers
	 */
	public OpenApiResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			SpringDocConfigProperties springDocConfigProperties,
			SpringDocProviders springDocProviders, SpringDocCustomizers springDocCustomizers ) {
		super(DEFAULT_GROUP_NAME, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, springDocConfigProperties, springDocProviders, springDocCustomizers);
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
	protected Mono<byte[]> openapiJson(ServerHttpRequest serverHttpRequest, String apiDocsUrl, Locale locale)
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
	protected Mono<byte[]> openapiYaml(ServerHttpRequest serverHttpRequest, String apiDocsUrl, Locale locale)
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
	protected void getPaths(Map<String, Object> restControllers, Locale locale, OpenAPI openAPI) {
		Optional<SpringWebProvider> springWebProviderOptional = springDocProviders.getSpringWebProvider();
		springWebProviderOptional.ifPresent(springWebProvider -> {
			Map<RequestMappingInfo, HandlerMethod> map = springWebProvider.getHandlerMethods();
			Optional<ActuatorProvider> actuatorProviderOptional = springDocProviders.getActuatorProvider();
			if (actuatorProviderOptional.isPresent() && springDocConfigProperties.isShowActuator()) {
				Map<RequestMappingInfo, HandlerMethod> actuatorMap = actuatorProviderOptional.get().getMethods();
				this.openAPIService.addTag(new HashSet<>(actuatorMap.values()), getTag());
				map.putAll(actuatorMap);
			}
			calculatePath(restControllers, map, locale, openAPI);
			getWebFluxRouterFunctionPaths(locale, openAPI);
		});
	}

	/**
	 * Calculate path.
	 *
	 * @param restControllers the rest controllers
	 * @param map the map
	 * @param locale the locale
	 */
	protected void calculatePath(Map<String, Object> restControllers, Map<RequestMappingInfo, HandlerMethod> map, Locale locale, OpenAPI openAPI) {
		TreeMap<RequestMappingInfo, HandlerMethod> methodTreeMap = new TreeMap<>(byReversedRequestMappingInfos());
		methodTreeMap.putAll(map);
		Optional<SpringWebProvider> springWebProviderOptional = springDocProviders.getSpringWebProvider();
		springWebProviderOptional.ifPresent(springWebProvider -> {
			for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : methodTreeMap.entrySet()) {
				RequestMappingInfo requestMappingInfo = entry.getKey();
				HandlerMethod handlerMethod = entry.getValue();
				Set<String> patterns = springWebProvider.getActivePatterns(requestMappingInfo);
				for (String operationPath : patterns) {
					Map<String, String> regexMap = new LinkedHashMap<>();
					operationPath = PathUtils.parsePath(operationPath, regexMap);
					String[] produces = requestMappingInfo.getProducesCondition().getProducibleMediaTypes().stream().map(MimeType::toString).toArray(String[]::new);
					String[] consumes = requestMappingInfo.getConsumesCondition().getConsumableMediaTypes().stream().map(MimeType::toString).toArray(String[]::new);
					String[] headers = requestMappingInfo.getHeadersCondition().getExpressions().stream().map(Object::toString).toArray(String[]::new);
					String[] params = requestMappingInfo.getParamsCondition().getExpressions().stream().map(Object::toString).toArray(String[]::new);
					if ((isRestController(restControllers, handlerMethod, operationPath) || isActuatorRestController(operationPath, handlerMethod))
							&& isFilterCondition(handlerMethod, operationPath, produces, consumes, headers)) {
						Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
						// default allowed requestmethods
						if (requestMethods.isEmpty())
							requestMethods = this.getDefaultAllowedHttpMethods();
						calculatePath(handlerMethod, operationPath, requestMethods, consumes, produces, headers, params, locale, openAPI);
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
	private Comparator<RequestMappingInfo> byReversedRequestMappingInfos() {
		return (o2, o1) -> o1.toString().compareTo(o2.toString());
	}

	/**
	 * Gets web flux router function paths.
	 * @param locale the locale
	 */
	protected void getWebFluxRouterFunctionPaths(Locale locale, OpenAPI openAPI) {
		Map<String, RouterFunction> routerBeans = Objects.requireNonNull(openAPIService.getContext()).getBeansOfType(RouterFunction.class);
		for (Map.Entry<String, RouterFunction> entry : routerBeans.entrySet()) {
			RouterFunction routerFunction = entry.getValue();
			RouterFunctionVisitor routerFunctionVisitor = new RouterFunctionVisitor();
			routerFunction.accept(routerFunctionVisitor);
			getRouterFunctionPaths(entry.getKey(), routerFunctionVisitor, locale, openAPI);
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
		openAPIService.setServerBaseUrl(serverUrl, serverHttpRequest);
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
