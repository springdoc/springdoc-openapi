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

import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.GenericParameterBuilder;
import org.springdoc.core.GenericResponseBuilder;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.PropertyResolverUtils;
import org.springdoc.core.RequestBodyBuilder;
import org.springdoc.core.ReturnTypeParser;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.ActuatorOpenApiCustomiser;
import org.springdoc.core.customizers.ActuatorOperationCustomizer;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springdoc.webflux.api.OpenApiResource;
import org.springdoc.webflux.core.converters.WebFluxSupportConverter;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.web.reactive.ControllerEndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.web.reactive.WebFluxEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;

/**
 * The type Spring doc web flux configuration.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocWebFluxConfiguration {

	/**
	 * Open api resource open api resource.
	 *
	 * @param openAPIBuilderObjectFactory the open api builder object factory 
	 * @param requestBuilder the request builder  
	 * @param responseBuilder the response builder  
	 * @param operationParser the operation parser  
	 * @param requestMappingHandlerMapping the request mapping handler mapping  
	 * @param operationCustomizers the operation customizers  
	 * @param openApiCustomisers the open api customisers  
	 * @param springDocConfigProperties the spring doc config properties  
	 * @param actuatorProvider the actuator provider  
	 * @return the open api resource
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	OpenApiResource openApiResource(ObjectFactory<OpenAPIBuilder> openAPIBuilderObjectFactory, AbstractRequestBuilder requestBuilder,
			GenericResponseBuilder responseBuilder, OperationBuilder operationParser,
			RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
			Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<OpenApiCustomiser>> openApiCustomisers,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<ActuatorProvider> actuatorProvider) {
		return new OpenApiResource(openAPIBuilderObjectFactory, requestBuilder,
				responseBuilder, operationParser,
				requestMappingHandlerMapping,operationCustomizers,
				openApiCustomisers, springDocConfigProperties,actuatorProvider);
	}

	/**
	 * Request builder request builder.
	 *
	 * @param parameterBuilder the parameter builder  
	 * @param requestBodyBuilder the request body builder  
	 * @param operationBuilder the operation builder  
	 * @param parameterCustomizers the parameter customizers  
	 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer  
	 * @return the request builder
	 */
	@Bean
	@ConditionalOnMissingBean
	RequestBuilder requestBuilder(GenericParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
			OperationBuilder operationBuilder,
			Optional<List<ParameterCustomizer>> parameterCustomizers,
			LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
		return new RequestBuilder(parameterBuilder, requestBodyBuilder,
				operationBuilder, parameterCustomizers, localSpringDocParameterNameDiscoverer);
	}

	/**
	 * Response builder generic response builder.
	 *
	 * @param operationBuilder the operation builder  
	 * @param returnTypeParsers the return type parsers  
	 * @param springDocConfigProperties the spring doc config properties  
	 * @param propertyResolverUtils the property resolver utils  
	 * @return the generic response builder
	 */
	@Bean
	@ConditionalOnMissingBean
	GenericResponseBuilder responseBuilder(OperationBuilder operationBuilder, List<ReturnTypeParser> returnTypeParsers, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils) {
		return new GenericResponseBuilder(operationBuilder, returnTypeParsers, springDocConfigProperties, propertyResolverUtils);
	}

	/**
	 * Web flux support converter web flux support converter.
	 *
	 * @return the web flux support converter
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	WebFluxSupportConverter webFluxSupportConverter() {
		return new WebFluxSupportConverter();
	}

	/**
	 * The type Spring doc web flux actuator configuration.
	 * @author bnasslahsen
	 */
	@ConditionalOnProperty(SPRINGDOC_SHOW_ACTUATOR)
	@ConditionalOnClass(WebFluxEndpointHandlerMapping.class)
	@ConditionalOnManagementPort(ManagementPortType.SAME)
	static class SpringDocWebFluxActuatorConfiguration {

		/**
		 * Actuator provider actuator provider.
		 *
		 * @param webFluxEndpointHandlerMapping the web flux endpoint handler mapping
		 * @param controllerEndpointHandlerMapping the controller endpoint handler mapping
		 * @return the actuator provider
		 */
		@Bean
		@ConditionalOnMissingBean
		ActuatorProvider actuatorProvider(WebFluxEndpointHandlerMapping webFluxEndpointHandlerMapping, ControllerEndpointHandlerMapping controllerEndpointHandlerMapping) {
			return new WebFluxActuatorProvider(webFluxEndpointHandlerMapping, controllerEndpointHandlerMapping);
		}

		/**
		 * Actuator customizer operation customizer.
		 *
		 * @param actuatorProvider the actuator provider  
		 * @return the operation customizer
		 */
		@Bean
		@Lazy(false)
		OperationCustomizer actuatorCustomizer(ActuatorProvider actuatorProvider) {
			return new ActuatorOperationCustomizer(actuatorProvider);
		}

		/**
		 * Actuator customizer open api customiser.
		 *
		 * @return the open api customiser
		 */
		@Bean
		@Lazy(false)
		OpenApiCustomiser actuatorOpenApiCustomiser() {
			return new ActuatorOpenApiCustomiser();
		}
	}
}
