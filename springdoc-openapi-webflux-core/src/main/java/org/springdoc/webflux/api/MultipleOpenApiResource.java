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

import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfigProperties.GroupConfig;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.ACTUATOR_DEFAULT_GROUP;

/**
 * The type Multiple open api resource.
 * @author bnasslahsen
 */
public abstract class MultipleOpenApiResource implements InitializingBean {

	/**
	 * The Grouped open apis.
	 */
	private final List<GroupedOpenApi> groupedOpenApis;

	/**
	 * The Default open api builder.
	 */
	private final ObjectFactory<OpenAPIService> defaultOpenAPIBuilder;

	/**
	 * The Request builder.
	 */
	private final AbstractRequestService requestBuilder;

	/**
	 * The Response builder.
	 */
	private final GenericResponseService responseBuilder;

	/**
	 * The Operation parser.
	 */
	private final OperationService operationParser;

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
			ObjectFactory<OpenAPIService> defaultOpenAPIBuilder, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
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
							return buildWebFluxOpenApiResource(item);
						}
				));
	}

	/**
	 * Build web flux open api resource open api resource.
	 *
	 * @param item the item
	 * @return the open api resource
	 */
	private OpenApiResource buildWebFluxOpenApiResource(GroupedOpenApi item) {
		if (!springDocConfigProperties.isUseManagementPort() && !ACTUATOR_DEFAULT_GROUP.equals(item.getGroup()))
			return new OpenApiWebfluxResource(item.getGroup(),
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
		else
			return new OpenApiActuatorResource(item.getGroup(),
					defaultOpenAPIBuilder,
					requestBuilder,
					responseBuilder,
					operationParser,
					requestMappingHandlerMapping,
					Optional.of(item.getOperationCustomizers()),
					Optional.of(item.getOpenApiCustomisers()),
					springDocConfigProperties,
					actuatorProvider);
	}

	/**
	 * Gets open api resource or throw.
	 *
	 * @param group the group
	 * @return the open api resource or throw
	 */
	protected OpenApiResource getOpenApiResourceOrThrow(String group) {
		OpenApiResource openApiResource = groupedOpenApiResources.get(group);
		if (openApiResource == null) {
			throw new OpenApiResourceNotFoundException("No OpenAPI resource found for group: " + group);
		}
		return openApiResource;
	}
}