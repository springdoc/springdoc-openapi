/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.webflux.core;

import java.util.List;
import java.util.Optional;

import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.GenericResponseBuilder;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.MultipleOpenApiSupportCondition;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.webflux.api.MultipleOpenApiResource;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;


@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@Conditional(MultipleOpenApiSupportCondition.class)
class MultipleOpenApiWebFluxConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	MultipleOpenApiResource multipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis,
			ObjectFactory<OpenAPIBuilder> defaultOpenAPIBuilder, AbstractRequestBuilder requestBuilder,
			Optional<List<OperationCustomizer>> operationCustomizers,
			GenericResponseBuilder responseBuilder, OperationBuilder operationParser,
			RequestMappingInfoHandlerMapping requestMappingHandlerMapping, SpringDocConfigProperties springDocConfigProperties) {
		return new MultipleOpenApiResource(groupedOpenApis,
				defaultOpenAPIBuilder, requestBuilder,
				responseBuilder, operationParser, operationCustomizers,
				requestMappingHandlerMapping, springDocConfigProperties);
	}
}