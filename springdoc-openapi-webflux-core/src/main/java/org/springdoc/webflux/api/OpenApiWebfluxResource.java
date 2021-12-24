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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocProviders;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL_YAML;

/**
 * The type Open api resource.
 * @author bnasslahsen
 */
@RestController
public class OpenApiWebfluxResource extends OpenApiResource {


	/**
	 * Instantiates a new Open api webflux resource.
	 *
	 * @param groupName the group name
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param operationCustomizers the operation customizers
	 * @param openApiCustomisers the open api customisers
	 * @param springDocConfigProperties the spring doc config properties
	 */
	public OpenApiWebfluxResource(String groupName, ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties, SpringDocProviders springDocProviders) {
		super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, operationCustomizers, openApiCustomisers, springDocConfigProperties, springDocProviders);
	}

	/**
	 * Instantiates a new Open api webflux resource.
	 *
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param operationCustomizers the operation customizers
	 * @param openApiCustomisers the open api customisers
	 * @param springDocConfigProperties the spring doc config properties
	 */
	@Autowired
	public OpenApiWebfluxResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties, SpringDocProviders springDocProviders) {
		super(openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, operationCustomizers, openApiCustomisers, springDocConfigProperties, springDocProviders);
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
	@Operation(hidden = true)
	@GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public Mono<String> openapiJson(ServerHttpRequest serverHttpRequest, @Value(API_DOCS_URL) String apiDocsUrl, Locale locale)
			throws JsonProcessingException {
		return super.openapiJson(serverHttpRequest, apiDocsUrl, locale);
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
	@Operation(hidden = true)
	@GetMapping(value = DEFAULT_API_DOCS_URL_YAML, produces = APPLICATION_OPENAPI_YAML)
	@Override
	public Mono<String> openapiYaml(ServerHttpRequest serverHttpRequest,
			@Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl, Locale locale) throws JsonProcessingException {
		return super.openapiYaml(serverHttpRequest, apiDocsUrl, locale);
	}

	/**
	 * Gets server url.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 * @return the server url
	 */
	@Override
	protected String getServerUrl(ServerHttpRequest serverHttpRequest, String apiDocsUrl) {
		String requestUrl = decode(serverHttpRequest.getURI().toString());
		final String prefix = findPathPrefix(this.openAPIService.getContext(), this.springDocConfigProperties);
		return requestUrl.substring(0, requestUrl.length() - apiDocsUrl.length()- prefix.length());
	}

	/**
	 * Finds path prefix.
	 *
	 * @param applicationContext the application context
	 * @param springDocConfigProperties the spring doc config properties
	 * @return the path prefix
	 */
	public static String findPathPrefix(ApplicationContext applicationContext, SpringDocConfigProperties springDocConfigProperties) {
		Map<String, RequestMappingHandlerMapping> beansOfTypeRequestMappingHandlerMapping = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);
		for (RequestMappingHandlerMapping requestMappingHandlerMapping : beansOfTypeRequestMappingHandlerMapping.values()) {
			Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
			List<Entry<RequestMappingInfo, HandlerMethod>> entries = new ArrayList<>(map.entrySet());
			for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : entries) {
				RequestMappingInfo requestMappingInfo = entry.getKey();
				PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
				Set<PathPattern> patterns = patternsRequestCondition.getPatterns();
				for (PathPattern pathPattern : patterns) {
					String operationPath = pathPattern.getPatternString();
					if (operationPath.endsWith(springDocConfigProperties.getApiDocs().getPath()))
						return operationPath.replace(springDocConfigProperties.getApiDocs().getPath(), StringUtils.EMPTY);
				}
			}
		}
		return StringUtils.EMPTY;
	}
}
