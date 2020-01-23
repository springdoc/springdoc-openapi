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

package org.springdoc.core;

import java.util.List;
import java.util.Optional;

import org.springdoc.api.OpenApiResource;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.ParameterCustomizer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocWebFluxConfiguration {

	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
	public OpenApiResource openApiResource(OpenAPIBuilder openAPIBuilder, AbstractRequestBuilder requestBuilder,
			AbstractResponseBuilder responseBuilder, OperationBuilder operationParser,
			RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
			Optional<List<OpenApiCustomiser>> openApiCustomisers) {
		return new OpenApiResource(openAPIBuilder, requestBuilder,
				responseBuilder, operationParser,
				requestMappingHandlerMapping,
				openApiCustomisers);
	}

	@Bean
	public ParameterBuilder parameterBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer, IgnoredParameterAnnotations ignoredParameterAnnotations) {
		return new ParameterBuilder(localSpringDocParameterNameDiscoverer, ignoredParameterAnnotations);
	}

	@Bean
	public RequestBuilder requestBuilder(AbstractParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
			OperationBuilder operationBuilder, Optional<List<OperationCustomizer>> operationCustomizers, Optional<List<ParameterCustomizer>> parameterCustomizers) {
		return new RequestBuilder(parameterBuilder, requestBodyBuilder,
				operationBuilder, operationCustomizers, parameterCustomizers);
	}

	@Bean
	public ResponseBuilder responseBuilder(OperationBuilder operationBuilder, List<ReturnTypeParser> returnTypeParsers) {
		return new ResponseBuilder(operationBuilder, returnTypeParsers);
	}

}
