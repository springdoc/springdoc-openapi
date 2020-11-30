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

package org.springdoc.webmvc.api;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.RepositoryRestResourceProvider;
import org.springdoc.core.SecurityOAuth2Provider;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.webmvc.core.RouterFunctionProvider;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL_YAML;

/**
 * The type Open api resource.
 * @author bnasslahsen
 */
@RestController
public class OpenApiWebMvcResource extends OpenApiResource {

	/**
	 * Instantiates a new Open api web mvc resource.
	 *
	 * @param groupName the group name
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param requestMappingHandlerMapping the request mapping handler mapping
	 * @param actuatorProvider the actuator provider
	 * @param operationCustomizers the operation customizers
	 * @param openApiCustomisers the open api customisers
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springSecurityOAuth2Provider the spring security o auth 2 provider
	 * @param routerFunctionProvider the router function provider
	 * @param repositoryRestResourceProvider the repository rest resource provider
	 */
	public OpenApiWebMvcResource(String groupName, ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, RequestMappingInfoHandlerMapping requestMappingHandlerMapping, Optional<ActuatorProvider> actuatorProvider, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties, Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider, Optional<RouterFunctionProvider> routerFunctionProvider, Optional<RepositoryRestResourceProvider> repositoryRestResourceProvider) {
		super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, requestMappingHandlerMapping, actuatorProvider, operationCustomizers, openApiCustomisers, springDocConfigProperties, springSecurityOAuth2Provider, routerFunctionProvider, repositoryRestResourceProvider);
	}

	/**
	 * Instantiates a new Open api web mvc resource.
	 *
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param requestMappingHandlerMapping the request mapping handler mapping
	 * @param actuatorProvider the actuator provider
	 * @param operationCustomizers the operation customizers
	 * @param openApiCustomisers the open api customisers
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springSecurityOAuth2Provider the spring security o auth 2 provider
	 * @param routerFunctionProvider the router function provider
	 * @param repositoryRestResourceProvider the repository rest resource provider
	 */
	@Autowired
	public OpenApiWebMvcResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, RequestMappingInfoHandlerMapping requestMappingHandlerMapping, Optional<ActuatorProvider> actuatorProvider, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties, Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider, Optional<RouterFunctionProvider> routerFunctionProvider, Optional<RepositoryRestResourceProvider> repositoryRestResourceProvider) {
		super(openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, requestMappingHandlerMapping, actuatorProvider, operationCustomizers, openApiCustomisers, springDocConfigProperties, springSecurityOAuth2Provider, routerFunctionProvider, repositoryRestResourceProvider);
	}

	/**
	 * Openapi json string.
	 *
	 * @param request the request
	 * @param apiDocsUrl the api docs url
	 * @return the string
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public String openapiJson(HttpServletRequest request, @Value(API_DOCS_URL) String apiDocsUrl)
			throws JsonProcessingException {
		return super.openapiJson(request, apiDocsUrl);
	}

	/**
	 * Openapi yaml string.
	 *
	 * @param request the request
	 * @param apiDocsUrl the api docs url
	 * @return the string
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value = DEFAULT_API_DOCS_URL_YAML, produces = APPLICATION_OPENAPI_YAML)
	@Override
	public String openapiYaml(HttpServletRequest request, @Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl)
			throws JsonProcessingException {
		return super.openapiYaml(request, apiDocsUrl);
	}

	@Override
	protected String getServerUrl(HttpServletRequest request, String apiDocsUrl) {
		String requestUrl = decode(request.getRequestURL().toString());
		return requestUrl.substring(0, requestUrl.length() - apiDocsUrl.length());
	}

}