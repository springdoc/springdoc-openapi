/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 *
 */

package org.springdoc.data.rest.core;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.data.rest.SpringRepositoryRestResourceProvider;

import org.springframework.data.rest.webmvc.ProfileController;
import org.springframework.data.rest.webmvc.alps.AlpsController;
import org.springframework.web.method.HandlerMethod;

/**
 * The type Data rest tags builder.
 * @author bnasslahsen
 */
public class DataRestTagsBuilder {

	/**
	 * The Open api builder.
	 */
	private OpenAPIBuilder openAPIBuilder;

	/**
	 * Instantiates a new Data rest tags builder.
	 *
	 * @param openAPIBuilder the open api builder
	 */
	public DataRestTagsBuilder(OpenAPIBuilder openAPIBuilder) {
		this.openAPIBuilder = openAPIBuilder;
	}

	/**
	 * Build search tags.
	 *
	 * @param operation the operation
	 * @param openAPI the open api
	 * @param handlerMethod the handler method
	 * @param domainType the domain type
	 */
	public void buildSearchTags(Operation operation, OpenAPI openAPI, HandlerMethod handlerMethod,
			Class<?> domainType) {
		buildTags(operation, openAPI, handlerMethod, domainType);
	}

	/**
	 * Build entity tags.
	 *
	 * @param operation the operation
	 * @param openAPI the open api
	 * @param handlerMethod the handler method
	 * @param domainType the domain type
	 */
	public void buildEntityTags(Operation operation, OpenAPI openAPI, HandlerMethod handlerMethod,
			Class<?> domainType) {
		buildTags(operation, openAPI, handlerMethod, domainType);
	}

	/**
	 * Build tags.
	 *
	 * @param operation the operation
	 * @param openAPI the open api
	 * @param handlerMethod the handler method
	 * @param domainType the domain type
	 */
	private void buildTags(Operation operation, OpenAPI openAPI, HandlerMethod handlerMethod,
			Class<?> domainType) {
		if (openAPIBuilder.isAutoTagClasses(operation)) {
			String tagName = handlerMethod.getBeanType().getSimpleName();
			if(SpringRepositoryRestResourceProvider.REPOSITORY_SCHEMA_CONTROLLER.equals(handlerMethod.getBeanType().getName())
			|| AlpsController.class.equals(handlerMethod.getBeanType()))
				tagName = ProfileController.class.getSimpleName();
			else if (domainType != null)
				tagName = tagName.replace("Repository", domainType.getSimpleName());
			operation.addTagsItem(OpenAPIBuilder.splitCamelCase(tagName));
		}
		openAPIBuilder.buildTags(handlerMethod, operation, openAPI);
	}
}
