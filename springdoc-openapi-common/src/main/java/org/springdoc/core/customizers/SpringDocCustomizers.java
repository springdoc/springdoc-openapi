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

import org.springdoc.core.filters.OpenApiMethodFilter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.Constants.LINKS_SCHEMA_CUSTOMISER;

/**
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


	private ApplicationContext context;

	/**
	 * The method filters to use.
	 */
	private final Optional<List<OpenApiMethodFilter>> methodFilters;


	/**
	 * Instantiates a new Spring doc customizers.
	 *
	 * @param openApiCustomizers the open api customizers
	 * @param operationCustomizers the operation customizers
	 * @param routerOperationCustomizers the router operation customizers
	 * @param dataRestRouterOperationCustomizers the data rest router operation customizers
	 * @param methodFilters the method filters
	 */
	public SpringDocCustomizers(Optional<List<OpenApiCustomiser>> openApiCustomizers,
			Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<RouterOperationCustomizer>> routerOperationCustomizers,
			Optional<List<DataRestRouterOperationCustomizer>> dataRestRouterOperationCustomizers,
			Optional<List<OpenApiMethodFilter>> methodFilters) {
		this.openApiCustomizers = openApiCustomizers;
		this.operationCustomizers = operationCustomizers;
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

	public Optional<List<OpenApiCustomiser>> getOpenApiCustomizers() {
		return openApiCustomizers;
	}

	public Optional<List<OperationCustomizer>> getOperationCustomizers() {
		return operationCustomizers;
	}

	public Optional<List<RouterOperationCustomizer>> getRouterOperationCustomizers() {
		return routerOperationCustomizers;
	}

	public Optional<List<DataRestRouterOperationCustomizer>> getDataRestRouterOperationCustomizers() {
		return dataRestRouterOperationCustomizers;
	}

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
}
