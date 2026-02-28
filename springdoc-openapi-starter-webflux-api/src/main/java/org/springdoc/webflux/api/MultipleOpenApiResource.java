/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package org.springdoc.webflux.api;

import java.util.List;
import java.util.Optional;

import org.springdoc.api.AbstractMultipleOpenApiResource;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;

import org.springframework.beans.factory.ObjectFactory;

import static org.springdoc.core.utils.Constants.ACTUATOR_DEFAULT_GROUP;

/**
 * The type Multiple open api resource.
 *
 * @author bnasslahsen
 */
public abstract class MultipleOpenApiResource extends AbstractMultipleOpenApiResource<OpenApiResource> {

	/**
	 * Instantiates a new Multiple open api resource.
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
	protected MultipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis,
			ObjectFactory<OpenAPIService> defaultOpenAPIBuilder, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			SpringDocConfigProperties springDocConfigProperties, SpringDocProviders springDocProviders, SpringDocCustomizers springDocCustomizers) {

		super(groupedOpenApis, defaultOpenAPIBuilder, requestBuilder, responseBuilder, operationParser, springDocConfigProperties, springDocProviders, springDocCustomizers);
	}

	@Override
	protected OpenApiResource buildOpenApiResource(GroupedOpenApi item) {
		if (!springDocConfigProperties.isUseManagementPort() && !ACTUATOR_DEFAULT_GROUP.equals(item.getGroup()))
			return new OpenApiWebfluxResource(item.getGroup(),
					defaultOpenAPIBuilder,
					requestBuilder,
					responseBuilder,
					operationParser,
					springDocConfigProperties,
					springDocProviders, new SpringDocCustomizers(Optional.of(item.getOpenApiCustomizers()), Optional.of(item.getOperationCustomizers()),
					Optional.of(item.getRouterOperationCustomizers()), Optional.of(item.getOpenApiMethodFilters()), Optional.empty(), Optional.empty())
			);
		else
			return new OpenApiActuatorResource(item.getGroup(),
					defaultOpenAPIBuilder,
					requestBuilder,
					responseBuilder,
					operationParser,
					springDocConfigProperties,
					springDocProviders, new SpringDocCustomizers(Optional.of(item.getOpenApiCustomizers()), Optional.of(item.getOperationCustomizers()),
					Optional.of(item.getRouterOperationCustomizers()), Optional.of(item.getOpenApiMethodFilters()), Optional.empty(), Optional.empty()));
	}

}
