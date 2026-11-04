/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.springdoc.api.v31.app11.controllers;

import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.webmvc.api.OpenApiWebMvcResource;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomOpenApiWebMvcResource extends OpenApiWebMvcResource {

	public CustomOpenApiWebMvcResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory,
			AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder,
			OperationService operationParser,
			SpringDocConfigProperties springDocConfigProperties,
			SpringDocProviders springDocProviders,
			SpringDocCustomizers springDocCustomizers) {
		super(openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, springDocConfigProperties, springDocProviders, springDocCustomizers);
	}
}