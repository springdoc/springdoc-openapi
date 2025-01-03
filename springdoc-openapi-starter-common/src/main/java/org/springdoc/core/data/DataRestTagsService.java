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

package org.springdoc.core.data;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.providers.SpringRepositoryRestResourceProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.SecurityService;

import org.springframework.data.rest.webmvc.ProfileController;
import org.springframework.data.rest.webmvc.alps.AlpsController;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;

/**
 * The type Data rest tags builder.
 *
 * @author bnasslahsen
 */
public class DataRestTagsService {

	/**
	 * The Open api builder.
	 */
	private final OpenAPIService openAPIService;

	/**
	 * Instantiates a new Data rest tags builder.
	 *
	 * @param openAPIService the open api builder
	 */
	public DataRestTagsService(OpenAPIService openAPIService) {
		this.openAPIService = openAPIService;
	}

	/**
	 * Build search tags.
	 *
	 * @param operation          the operation
	 * @param handlerMethod      the handler method
	 * @param dataRestRepository the repository data rest
	 * @param method             the method
	 */
	public void buildSearchTags(Operation operation, HandlerMethod handlerMethod,
			DataRestRepository dataRestRepository, Method method) {
		buildTags(operation, handlerMethod, dataRestRepository, method);
	}

	/**
	 * Build entity tags.
	 *
	 * @param operation          the operation
	 * @param handlerMethod      the handler method
	 * @param dataRestRepository the repository data rest
	 */
	public void buildEntityTags(Operation operation, HandlerMethod handlerMethod,
			DataRestRepository dataRestRepository) {
		buildTags(operation, handlerMethod, dataRestRepository, null);
	}

	/**
	 * Build tags.
	 *
	 * @param operation          the operation
	 * @param handlerMethod      the handler method
	 * @param dataRestRepository the repository data rest
	 * @param method             the method
	 */
	private void buildTags(Operation operation, HandlerMethod handlerMethod,
			DataRestRepository dataRestRepository, Method method) {
		String tagName = handlerMethod.getBeanType().getSimpleName();
		if (SpringRepositoryRestResourceProvider.REPOSITORY_SCHEMA_CONTROLLER.equals(handlerMethod.getBeanType().getName())
				|| AlpsController.class.equals(handlerMethod.getBeanType())
				|| ProfileController.class.equals(handlerMethod.getBeanType())) {
			tagName = ProfileController.class.getSimpleName();
			operation.addTagsItem(OpenAPIService.splitCamelCase(tagName));
		}
		else {
			Class<?> domainType = dataRestRepository.getDomainType();
			Set<Tag> tags = new HashSet<>();
			Set<String> tagsStr = new HashSet<>();
			Class<?> repositoryType = dataRestRepository.getRepositoryType();
			openAPIService.buildTagsFromClass(repositoryType, tags, tagsStr, dataRestRepository.getLocale());
			if (!CollectionUtils.isEmpty(tagsStr))
				tagsStr.forEach(operation::addTagsItem);
			else {
				tagName = tagName.replace("Repository", domainType.getSimpleName());
				operation.addTagsItem(OpenAPIService.splitCamelCase(tagName));
			}
			final SecurityService securityParser = openAPIService.getSecurityParser();
			Set<io.swagger.v3.oas.annotations.security.SecurityRequirement> allSecurityTags = securityParser.getSecurityRequirementsForClass(repositoryType);
			if (method != null)
				allSecurityTags = securityParser.getSecurityRequirementsForMethod(method, allSecurityTags);
			if (!CollectionUtils.isEmpty(allSecurityTags))
				securityParser.buildSecurityRequirement(allSecurityTags.toArray(new io.swagger.v3.oas.annotations.security.SecurityRequirement[0]), operation);
		}
	}
}
