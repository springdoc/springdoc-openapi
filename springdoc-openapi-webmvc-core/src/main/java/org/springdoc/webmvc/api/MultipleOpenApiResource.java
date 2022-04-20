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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfigProperties.GroupConfig;
import org.springdoc.core.SpringDocProviders;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.filters.GlobalOpenApiMethodFilter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import static org.springdoc.core.Constants.ACTUATOR_DEFAULT_GROUP;

/**
 * The type Web mvc multiple open api resource.
 * @author bnasslahsen
 */
public abstract class MultipleOpenApiResource implements InitializingBean, ApplicationContextAware {

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
	 * The Spring doc providers.
	 */
	private final SpringDocProviders springDocProviders;

	/**
	 * The Spring doc config properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The Grouped open api resources.
	 */
	private Map<String, OpenApiResource> groupedOpenApiResources;

	/**
	 * The Application context.
	 */
	protected ApplicationContext applicationContext;

	/**
	 * Instantiates a new Multiple open api resource.
	 *
	 * @param groupedOpenApis the grouped open apis
	 * @param defaultOpenAPIBuilder the default open api builder
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springDocProviders the spring doc providers
	 */
	public MultipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis,
			ObjectFactory<OpenAPIService> defaultOpenAPIBuilder, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			SpringDocConfigProperties springDocConfigProperties, SpringDocProviders springDocProviders) {

		this.groupedOpenApis = groupedOpenApis;
		this.defaultOpenAPIBuilder = defaultOpenAPIBuilder;
		this.requestBuilder = requestBuilder;
		this.responseBuilder = responseBuilder;
		this.operationParser = operationParser;
		this.springDocConfigProperties = springDocConfigProperties;
		this.springDocProviders = springDocProviders;
	}

	@Override
	public void afterPropertiesSet() {
		Map<String, GlobalOpenApiCustomizer> globalOpenApiCustomizerMap = applicationContext.getBeansOfType(GlobalOpenApiCustomizer.class);
		Map<String, GlobalOperationCustomizer> globalOperationCustomizerMap = applicationContext.getBeansOfType(GlobalOperationCustomizer.class);
		Map<String, GlobalOpenApiMethodFilter> globalOpenApiMethodFilterMap = applicationContext.getBeansOfType(GlobalOpenApiMethodFilter.class);

		this.groupedOpenApis.forEach(groupedOpenApi -> groupedOpenApi
				.addAllOpenApiCustomizer(globalOpenApiCustomizerMap.values())
				.addAllOperationCustomizer(globalOperationCustomizerMap.values())
				.addAllOpenApiMethodFilter(globalOpenApiMethodFilterMap.values())
		);
		
		this.groupedOpenApiResources = groupedOpenApis.stream()
				.collect(Collectors.toMap(GroupedOpenApi::getGroup, item ->
						{
							GroupConfig groupConfig = new GroupConfig(item.getGroup(), item.getPathsToMatch(), item.getPackagesToScan(), item.getPackagesToExclude(), item.getPathsToExclude(), item.getProducesToMatch(), item.getConsumesToMatch(), item.getHeadersToMatch(), item.getDisplayName());
							springDocConfigProperties.addGroupConfig(groupConfig);
							return buildWebMvcOpenApiResource(item);
						}
				));
	}

	/**
	 * Build web mvc open api resource open api resource.
	 *
	 * @param item the item
	 * @return the open api resource
	 */
	private OpenApiResource buildWebMvcOpenApiResource(GroupedOpenApi item) {
		if (!springDocConfigProperties.isUseManagementPort() && !ACTUATOR_DEFAULT_GROUP.equals(item.getGroup()))
			return new OpenApiWebMvcResource(item.getGroup(),
					defaultOpenAPIBuilder,
					requestBuilder,
					responseBuilder,
					operationParser,
					Optional.of(item.getOperationCustomizers()),
					Optional.of(item.getOpenApiCustomisers()),
					Optional.of(item.getOpenApiMethodFilters()),
					springDocConfigProperties, springDocProviders

			);
		else
			return new OpenApiActuatorResource(item.getGroup(),
					defaultOpenAPIBuilder,
					requestBuilder,
					responseBuilder,
					operationParser,
					Optional.of(item.getOperationCustomizers()),
					Optional.of(item.getOpenApiCustomisers()),
					Optional.of(item.getOpenApiMethodFilters()),
					springDocConfigProperties, springDocProviders
			);
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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
