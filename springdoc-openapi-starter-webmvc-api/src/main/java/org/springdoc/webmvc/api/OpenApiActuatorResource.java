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

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springdoc.core.utils.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_ACTUATOR_URL;
import static org.springdoc.core.utils.Constants.DEFAULT_YAML_API_DOCS_ACTUATOR_PATH;
import static org.springdoc.core.utils.Constants.SPRINGDOC_ENABLE_DEFAULT_API_DOCS;
import static org.springdoc.core.utils.Constants.YAML;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Open api actuator resource.
 *
 * @author bnasslashen
 */
@RestControllerEndpoint(id = DEFAULT_API_DOCS_ACTUATOR_URL)
@ConditionalOnProperty(name = SPRINGDOC_ENABLE_DEFAULT_API_DOCS, havingValue = "true", matchIfMissing = true)
public class OpenApiActuatorResource extends OpenApiResource {

	/**
	 * Instantiates a new Open api actuator resource.
	 *
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder              the request builder
	 * @param responseBuilder             the response builder
	 * @param operationParser             the operation parser
	 * @param springDocConfigProperties   the spring doc config properties
	 * @param springDocProviders          the spring doc providers
	 * @param springDocCustomizers        the spring doc customizers
	 */
	public OpenApiActuatorResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory,
			AbstractRequestService requestBuilder, GenericResponseService responseBuilder,
			OperationService operationParser,
			SpringDocConfigProperties springDocConfigProperties,
			SpringDocProviders springDocProviders,  SpringDocCustomizers springDocCustomizers) {
		super(openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, springDocConfigProperties, springDocProviders, springDocCustomizers);
	}

	/**
	 * Instantiates a new Open api actuator resource.
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
	public OpenApiActuatorResource(String groupName, ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory,
			AbstractRequestService requestBuilder, GenericResponseService responseBuilder,
			OperationService operationParser,SpringDocConfigProperties springDocConfigProperties,
			SpringDocProviders springDocProviders,  SpringDocCustomizers springDocCustomizers) {
		super(groupName, openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser,
				springDocConfigProperties, springDocProviders, springDocCustomizers);
	}

	/**
	 * Openapi json string.
	 *
	 * @param request the request
	 * @param locale  the locale
	 * @return the string
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value = DEFAULT_PATH_SEPARATOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public byte[] openapiJson(HttpServletRequest request, Locale locale)
			throws JsonProcessingException {
		return super.openapiJson(request, EMPTY, locale);
	}


	/**
	 * Openapi yaml string.
	 *
	 * @param request the request
	 * @param locale  the locale
	 * @return the string
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value = DEFAULT_YAML_API_DOCS_ACTUATOR_PATH, produces = APPLICATION_OPENAPI_YAML)
	public byte[] openapiYaml(HttpServletRequest request, Locale locale)
			throws JsonProcessingException {
		return super.openapiYaml(request, YAML, locale);
	}


	@Override
	protected String getServerUrl(HttpServletRequest request, String apiDocsUrl) {
		return getActuatorURI(request.getScheme(), request.getServerName()).toString();
	}

}
