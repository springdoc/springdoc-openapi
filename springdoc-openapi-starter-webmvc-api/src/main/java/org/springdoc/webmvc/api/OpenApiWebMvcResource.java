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

package org.springdoc.webmvc.api;

import java.util.Locale;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springdoc.core.utils.Constants.API_DOCS_URL;
import static org.springdoc.core.utils.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL_YAML;
import static org.springdoc.core.utils.Constants.SPRINGDOC_ENABLE_DEFAULT_API_DOCS;

/**
 * The type Open api resource.
 *
 * @author bnasslahsen
 */
@RestController
@ConditionalOnProperty(name = SPRINGDOC_ENABLE_DEFAULT_API_DOCS, havingValue = "true", matchIfMissing = true)
public class OpenApiWebMvcResource extends OpenApiResource {

	/**
	 * Instantiates a new Open api web mvc resource.
	 *
	 * @param groupName                   the group name
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder              the request builder
	 * @param responseBuilder             the response builder
	 * @param operationParser             the operation parser
	 * @param springDocConfigProperties   the spring doc config properties
	 * @param springDocProviders          the spring doc providers
	 * @param springDocCustomizers        the spring doc customizers
	 */
	public OpenApiWebMvcResource(String groupName, ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			SpringDocConfigProperties springDocConfigProperties, SpringDocProviders springDocProviders,
			SpringDocCustomizers springDocCustomizers) {
		super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, springDocConfigProperties, springDocProviders,springDocCustomizers);
	}

	/**
	 * Instantiates a new Open api web mvc resource.
	 *
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder              the request builder
	 * @param responseBuilder             the response builder
	 * @param operationParser             the operation parser
	 * @param springDocConfigProperties   the spring doc config properties
	 * @param springDocProviders          the spring doc providers
	 * @param springDocCustomizers        the spring doc customizers
	 */
	@Autowired
	public OpenApiWebMvcResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder, GenericResponseService responseBuilder,
			OperationService operationParser, SpringDocConfigProperties springDocConfigProperties, SpringDocProviders springDocProviders,  SpringDocCustomizers springDocCustomizers) {
		super(openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser,  springDocConfigProperties, springDocProviders, springDocCustomizers);
	}

	/**
	 * Openapi json string.
	 *
	 * @param request the request
	 * @param apiDocsUrl the api docs url
	 * @param locale the locale
	 * @return the string
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public byte[] openapiJson(HttpServletRequest request, @Value(API_DOCS_URL) String apiDocsUrl, Locale locale)
			throws JsonProcessingException {
		return super.openapiJson(request, apiDocsUrl, locale);
	}

	/**
	 * Openapi yaml string.
	 *
	 * @param request the request
	 * @param apiDocsUrl the api docs url
	 * @param locale the locale
	 * @return the string
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value = DEFAULT_API_DOCS_URL_YAML, produces = APPLICATION_OPENAPI_YAML)
	@Override
	public byte[] openapiYaml(HttpServletRequest request, @Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl, Locale locale)
			throws JsonProcessingException {
		return super.openapiYaml(request, apiDocsUrl, locale);
	}

	/**
	 * Gets server url.
	 *
	 * @param request the request
	 * @param apiDocsUrl the api docs url
	 * @return the server url
	 */
	@Override
	protected String getServerUrl(HttpServletRequest request, String apiDocsUrl) {
		String requestUrl = decode(request.getRequestURL().toString());
		Optional<SpringWebProvider> springWebProviderOptional = springDocProviders.getSpringWebProvider();
		String prefix = StringUtils.EMPTY;
		if (springWebProviderOptional.isPresent())
			prefix = springWebProviderOptional.get().findPathPrefix(springDocConfigProperties);
		return requestUrl.substring(0, requestUrl.length() - apiDocsUrl.length() - prefix.length());
	}

}