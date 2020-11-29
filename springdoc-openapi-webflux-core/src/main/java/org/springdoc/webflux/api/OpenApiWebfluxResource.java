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
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL_YAML;

/**
 * The type Open api resource.
 * @author bnasslahsen
 */
@RestController
public class OpenApiWebfluxResource extends OpenApiResource {


	public OpenApiWebfluxResource(String groupName, ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, RequestMappingInfoHandlerMapping requestMappingHandlerMapping, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties, Optional<ActuatorProvider> actuatorProvider) {
		super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, requestMappingHandlerMapping, operationCustomizers, openApiCustomisers, springDocConfigProperties, actuatorProvider);
	}

	@Autowired
	public OpenApiWebfluxResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, RequestMappingInfoHandlerMapping requestMappingHandlerMapping, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties, Optional<ActuatorProvider> actuatorProvider) {
		super(openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, requestMappingHandlerMapping, operationCustomizers, openApiCustomisers, springDocConfigProperties, actuatorProvider);
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
		return super.openapiJson(serverHttpRequest, apiDocsUrl);
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
		return super.openapiYaml(serverHttpRequest, apiDocsUrl);
	}
}
