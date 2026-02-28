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

package org.springdoc.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties.GroupConfig;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;

/**
 * The type Abstract multiple open api resource.
 *
 * @param <R> the type of OpenAPI resource
 * @author bnasslahsen
 */
public abstract class AbstractMultipleOpenApiResource<R extends AbstractOpenApiResource> implements InitializingBean {

	/**
	 * The Grouped open apis.
	 */
	private final List<GroupedOpenApi> groupedOpenApis;

	/**
	 * The Default open api builder.
	 */
	protected final ObjectFactory<OpenAPIService> defaultOpenAPIBuilder;

	/**
	 * The Request builder.
	 */
	protected final AbstractRequestService requestBuilder;

	/**
	 * The Response builder.
	 */
	protected final GenericResponseService responseBuilder;

	/**
	 * The Operation parser.
	 */
	protected final OperationService operationParser;

	/**
	 * The Spring doc config properties.
	 */
	protected final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The Spring doc providers.
	 */
	protected final SpringDocProviders springDocProviders;

	/**
	 * The Spring doc customizers.
	 */
	private final SpringDocCustomizers springDocCustomizers;

	/**
	 * The Grouped open api resources.
	 */
	private Map<String, R> groupedOpenApiResources;

	/**
	 * Instantiates a new Abstract multiple open api resource.
	 *
	 * @param groupedOpenApis           the grouped open apis
	 * @param defaultOpenAPIBuilder     the default open api builder
	 * @param requestBuilder            the request builder
	 * @param responseBuilder           the response builder
	 * @param operationParser           the operation parser
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springDocProviders        the spring doc providers
	 * @param springDocCustomizers      the spring doc customizers
	 */
	protected AbstractMultipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis,
			ObjectFactory<OpenAPIService> defaultOpenAPIBuilder, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			SpringDocConfigProperties springDocConfigProperties, SpringDocProviders springDocProviders, SpringDocCustomizers springDocCustomizers) {

		this.groupedOpenApis = groupedOpenApis;
		this.defaultOpenAPIBuilder = defaultOpenAPIBuilder;
		this.requestBuilder = requestBuilder;
		this.responseBuilder = responseBuilder;
		this.operationParser = operationParser;
		this.springDocConfigProperties = springDocConfigProperties;
		this.springDocProviders = springDocProviders;
		this.springDocCustomizers = springDocCustomizers;
	}

	@Override
	public void afterPropertiesSet() {
		this.groupedOpenApis.forEach(groupedOpenApi -> {
					springDocCustomizers.getGlobalOpenApiCustomizers().ifPresent(groupedOpenApi::addAllOpenApiCustomizer);
					springDocCustomizers.getGlobalOperationCustomizers().ifPresent(groupedOpenApi::addAllOperationCustomizer);
					springDocCustomizers.getGlobalOpenApiMethodFilters().ifPresent(groupedOpenApi::addAllOpenApiMethodFilter);
				}
		);

		this.groupedOpenApiResources = groupedOpenApis.stream()
				.collect(Collectors.toMap(GroupedOpenApi::getGroup, item ->
						{
							GroupConfig groupConfig = new GroupConfig(item.getGroup(), item.getPathsToMatch(), item.getPackagesToScan(), item.getPackagesToExclude(), item.getPathsToExclude(), item.getProducesToMatch(), item.getConsumesToMatch(), item.getHeadersToMatch(), item.getDisplayName());
							springDocConfigProperties.addGroupConfig(groupConfig);
							return buildOpenApiResource(item);
						},
						(existingValue, newValue) -> existingValue // choice to keep the existing value
				));
	}

	/**
	 * Build open api resource.
	 *
	 * @param item the grouped open api
	 * @return the open api resource
	 */
	protected abstract R buildOpenApiResource(GroupedOpenApi item);

	/**
	 * Gets open api resource or throw.
	 *
	 * @param group the group
	 * @return the open api resource or throw
	 */
	protected R getOpenApiResourceOrThrow(String group) {
		R openApiResource = groupedOpenApiResources.get(group);
		if (openApiResource == null) {
			throw new OpenApiResourceNotFoundException("No OpenAPI resource found for group: " + group);
		}
		return openApiResource;
	}

}
