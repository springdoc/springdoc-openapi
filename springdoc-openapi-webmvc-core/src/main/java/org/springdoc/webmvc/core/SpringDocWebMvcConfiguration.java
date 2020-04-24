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

package org.springdoc.webmvc.core;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.GenericParameterBuilder;
import org.springdoc.core.GenericResponseBuilder;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.PropertyResolverUtils;
import org.springdoc.core.RequestBodyBuilder;
import org.springdoc.core.ReturnTypeParser;
import org.springdoc.core.SecurityOAuth2Provider;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springdoc.webmvc.api.ActuatorProvider;
import org.springdoc.webmvc.api.OpenApiResource;

import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.DEFAULT_GROUP_NAME;
import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocWebMvcConfiguration {

	@Bean
	@ConditionalOnMissingBean(name = "openApiResource")
	@Lazy(false)
	OpenApiResource openApiResource(OpenAPIBuilder openAPIBuilder, AbstractRequestBuilder requestBuilder,
			GenericResponseBuilder responseBuilder, OperationBuilder operationParser,
			RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
			Optional<ActuatorProvider> servletContextProvider,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<List<OpenApiCustomiser>> openApiCustomisers,
			Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider) {
		return new OpenApiResource(DEFAULT_GROUP_NAME, openAPIBuilder, requestBuilder,
				responseBuilder, operationParser,
				requestMappingHandlerMapping, servletContextProvider,
				openApiCustomisers, springDocConfigProperties, springSecurityOAuth2Provider);
	}

	@Bean
	@ConditionalOnMissingBean(name = "requestBuilder")
	RequestBuilder requestBuilder(GenericParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
			OperationBuilder operationBuilder, Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<ParameterCustomizer>> parameterCustomizers,
			LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
		return new RequestBuilder(parameterBuilder, requestBodyBuilder,
				operationBuilder, operationCustomizers, parameterCustomizers, localSpringDocParameterNameDiscoverer);
	}

	@Bean
	@ConditionalOnMissingBean(name = "responseBuilder")
	GenericResponseBuilder responseBuilder(OperationBuilder operationBuilder, List<ReturnTypeParser> returnTypeParsers, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils) {
		return new GenericResponseBuilder(operationBuilder, returnTypeParsers, springDocConfigProperties, propertyResolverUtils);
	}

	@ConditionalOnProperty(SPRINGDOC_SHOW_ACTUATOR)
	@ConditionalOnClass(WebMvcEndpointHandlerMapping.class)
	class SpringDocWebMvcActuatorConfiguration {

		@Bean
		@ConditionalOnMissingBean(name = "actuatorProvider")
		ActuatorProvider actuatorProvider(WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping) {
			return new ActuatorProvider(webMvcEndpointHandlerMapping);
		}

		@Bean
		@Lazy(false)
		OperationCustomizer actuatorCustomizer(ActuatorProvider actuatorProvider) {
			return new OperationCustomizer() {
				private int methodCount;

				@Override
				public Operation customize(Operation operation, HandlerMethod handlerMethod) {
					if (operation.getTags() != null && operation.getTags().contains(actuatorProvider.getTag().getName())) {
						operation.setSummary(handlerMethod.toString());
						operation.setOperationId(operation.getOperationId() + "_" + methodCount++);
					}
					return operation;
				}
			};
		}

	}
}
