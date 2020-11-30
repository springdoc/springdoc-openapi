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

package org.springdoc.webflux.core;

import java.util.List;
import java.util.Optional;

import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.MultipleOpenApiSupportCondition;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.webflux.api.MultipleOpenApiActuatorResource;
import org.springdoc.webflux.api.MultipleOpenApiWebFluxResource;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.web.reactive.WebFluxEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_USE_MANAGEMENT_PORT;


/**
 * The type Multiple open api web flux configuration.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@Conditional(MultipleOpenApiSupportCondition.class)
public class MultipleOpenApiSupportConfiguration {

	/**
	 * Multiple open api resource multiple open api resource.
	 *
	 * @param groupedOpenApis the grouped open apis
	 * @param defaultOpenAPIBuilder the default open api builder
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param requestMappingHandlerMapping the request mapping handler mapping
	 * @param springDocConfigProperties the spring doc config properties
	 * @param actuatorProvider the actuator provider
	 * @return the multiple open api resource
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	@Lazy(false)
	MultipleOpenApiWebFluxResource multipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis,
			ObjectFactory<OpenAPIService> defaultOpenAPIBuilder, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<ActuatorProvider> actuatorProvider) {
		return new MultipleOpenApiWebFluxResource(groupedOpenApis,
				defaultOpenAPIBuilder, requestBuilder,
				responseBuilder, operationParser,
				requestMappingHandlerMapping,
				springDocConfigProperties,
				actuatorProvider);
	}

	/**
	 * The type Spring doc web mvc actuator different configuration.
	 * @author bnasslashen
	 */
	@ConditionalOnClass(WebFluxEndpointHandlerMapping.class)
	@ConditionalOnManagementPort(ManagementPortType.DIFFERENT)
	static class SpringDocWebMvcActuatorDifferentConfiguration {

		/**
		 * Multiple open api actuator resource multiple open api actuator resource.
		 *
		 * @param groupedOpenApis the grouped open apis
		 * @param defaultOpenAPIBuilder the default open api builder
		 * @param requestBuilder the request builder
		 * @param responseBuilder the response builder
		 * @param operationParser the operation parser
		 * @param requestMappingHandlerMapping the request mapping handler mapping
		 * @param springDocConfigProperties the spring doc config properties
		 * @param actuatorProvider the actuator provider
		 * @return the multiple open api actuator resource
		 */
		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(SPRINGDOC_USE_MANAGEMENT_PORT)
		@Lazy(false)
		MultipleOpenApiActuatorResource multipleOpenApiActuatorResource(List<GroupedOpenApi> groupedOpenApis,
				ObjectFactory<OpenAPIService> defaultOpenAPIBuilder, AbstractRequestService requestBuilder,
				GenericResponseService responseBuilder, OperationService operationParser,
				RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
				SpringDocConfigProperties springDocConfigProperties,
				Optional<ActuatorProvider> actuatorProvider) {
			return new MultipleOpenApiActuatorResource(groupedOpenApis, defaultOpenAPIBuilder, requestBuilder,
					responseBuilder, operationParser, requestMappingHandlerMapping,
					springDocConfigProperties, actuatorProvider);
		}
	}
}