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
package org.springdoc.core.customizers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springdoc.core.filters.GlobalOpenApiMethodFilter;
import org.springdoc.core.filters.OpenApiMethodFilter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.Constants.LINKS_SCHEMA_CUSTOMISER;

/**
 * The type Spring doc customizers.
 *
 * @author bnasslahsen
 */
public class SpringDocCustomizers implements ApplicationContextAware, InitializingBean {

	/**
	 * The Open api customisers.
	 */
	private final Optional<List<OpenApiCustomiser>> openApiCustomizers;

	/**
	 * The Operation customizers.
	 */
	private final Optional<List<OperationCustomizer>> operationCustomizers;


	/**
	 * The RouterOperation customizers.
	 */
	private final Optional<List<RouterOperationCustomizer>> routerOperationCustomizers;

	/**
	 * The Data rest router operation customizers.
	 */
	private final Optional<List<DataRestRouterOperationCustomizer>> dataRestRouterOperationCustomizers;


	/**
	 * The Context.
	 */
	private ApplicationContext context;

	/**
	 * The method filters to use.
	 */
	private final Optional<List<OpenApiMethodFilter>> methodFilters;


	/**
	 * The Global open api customizers.
	 */
	private Optional<List<GlobalOpenApiCustomizer>> globalOpenApiCustomizers;

	/**
	 * The Global operation customizers.
	 */
	private Optional<List<GlobalOperationCustomizer>> globalOperationCustomizers;

	/**
	 * The Global open api method filters.
	 */
	private Optional<List<GlobalOpenApiMethodFilter>> globalOpenApiMethodFilters;

	/**
	 * Instantiates a new Spring doc customizers.
	 *
	 * @param openApiCustomizers the open api customizers
	 * @param operationCustomizers the operation customizers
	 * @param routerOperationCustomizers the router operation customizers
	 * @param dataRestRouterOperationCustomizers the data rest router operation customizers
	 * @param methodFilters the method filters
	 * @param globalOpenApiCustomizers the global open api customizers
	 * @param globalOperationCustomizers the global operation customizers
	 * @param globalOpenApiMethodFilters the global open api method filters
	 */
	public SpringDocCustomizers(Optional<List<OpenApiCustomiser>> openApiCustomizers,
			Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<RouterOperationCustomizer>> routerOperationCustomizers,
			Optional<List<DataRestRouterOperationCustomizer>> dataRestRouterOperationCustomizers,
			Optional<List<OpenApiMethodFilter>> methodFilters, 	Optional<List<GlobalOpenApiCustomizer>> globalOpenApiCustomizers,
			Optional<List<GlobalOperationCustomizer>> globalOperationCustomizers,
			Optional<List<GlobalOpenApiMethodFilter>> globalOpenApiMethodFilters) {
		this.openApiCustomizers = openApiCustomizers;
		this.operationCustomizers = operationCustomizers;
		this.globalOpenApiCustomizers = globalOpenApiCustomizers;
		this.globalOperationCustomizers = globalOperationCustomizers;
		this.globalOpenApiMethodFilters = globalOpenApiMethodFilters;
		operationCustomizers.ifPresent(customizers -> customizers.removeIf(Objects::isNull));
		this.routerOperationCustomizers = routerOperationCustomizers;
		this.dataRestRouterOperationCustomizers = dataRestRouterOperationCustomizers;
		this.methodFilters = methodFilters;
	}

	/**
	 * Instantiates a new Spring doc customizers.
	 *
	 * @param openApiCustomizers the open api customizers
	 * @param operationCustomizers the operation customizers
	 * @param routerOperationCustomizers the router operation customizers
	 * @param openApiMethodFilters the open api method filters
	 */
	public SpringDocCustomizers(Optional<List<OpenApiCustomiser>> openApiCustomizers, Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<RouterOperationCustomizer>> routerOperationCustomizers, Optional<List<OpenApiMethodFilter>> openApiMethodFilters) {
		this.openApiCustomizers = openApiCustomizers;
		this.operationCustomizers = operationCustomizers;
		this.routerOperationCustomizers = routerOperationCustomizers;
		this.methodFilters = openApiMethodFilters;
		this.dataRestRouterOperationCustomizers = Optional.empty();
	}

	/**
	 * Gets open api customizers.
	 *
	 * @return the open api customizers
	 */
	public Optional<List<OpenApiCustomiser>> getOpenApiCustomizers() {
		return openApiCustomizers;
	}

	/**
	 * Gets operation customizers.
	 *
	 * @return the operation customizers
	 */
	public Optional<List<OperationCustomizer>> getOperationCustomizers() {
		return operationCustomizers;
	}

	/**
	 * Gets router operation customizers.
	 *
	 * @return the router operation customizers
	 */
	public Optional<List<RouterOperationCustomizer>> getRouterOperationCustomizers() {
		return routerOperationCustomizers;
	}

	/**
	 * Gets data rest router operation customizers.
	 *
	 * @return the data rest router operation customizers
	 */
	public Optional<List<DataRestRouterOperationCustomizer>> getDataRestRouterOperationCustomizers() {
		return dataRestRouterOperationCustomizers;
	}

	/**
	 * Gets method filters.
	 *
	 * @return the method filters
	 */
	public Optional<List<OpenApiMethodFilter>> getMethodFilters() {
		return methodFilters;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@Override
	public void afterPropertiesSet() {
		//add the default customizers
		Map<String, OpenApiCustomiser> existingOpenApiCustomizers = context.getBeansOfType(OpenApiCustomiser.class);
		if (!CollectionUtils.isEmpty(existingOpenApiCustomizers) && existingOpenApiCustomizers.containsKey(LINKS_SCHEMA_CUSTOMISER))
			this.openApiCustomizers.ifPresent(openApiCustomizersList -> openApiCustomizersList.add(existingOpenApiCustomizers.get(LINKS_SCHEMA_CUSTOMISER)));
	}

	/**
	 * Gets global open api customizers.
	 *
	 * @return the global open api customizers
	 */
	public Optional<List<GlobalOpenApiCustomizer>> getGlobalOpenApiCustomizers() {
		return globalOpenApiCustomizers;
	}

	/**
	 * Gets global operation customizers.
	 *
	 * @return the global operation customizers
	 */
	public Optional<List<GlobalOperationCustomizer>> getGlobalOperationCustomizers() {
		return globalOperationCustomizers;
	}

	/**
	 * Gets global open api method filters.
	 *
	 * @return the global open api method filters
	 */
	public Optional<List<GlobalOpenApiMethodFilter>> getGlobalOpenApiMethodFilters() {
		return globalOpenApiMethodFilters;
	}
}
