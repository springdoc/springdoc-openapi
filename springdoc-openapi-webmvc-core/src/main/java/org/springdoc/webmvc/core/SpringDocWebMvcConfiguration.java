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

package org.springdoc.webmvc.core;

import java.util.List;
import java.util.Optional;

import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.GenericParameterService;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.PropertyResolverUtils;
import org.springdoc.core.RepositoryRestResourceProvider;
import org.springdoc.core.RequestBodyService;
import org.springdoc.core.ReturnTypeParser;
import org.springdoc.core.SecurityOAuth2Provider;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.customizers.ActuatorOpenApiCustomizer;
import org.springdoc.core.customizers.ActuatorOperationCustomizer;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springdoc.webmvc.api.OpenApiActuatorResource;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springdoc.webmvc.api.OpenApiRouter;
import org.springdoc.webmvc.api.RouterFunctionProvider;
import org.springdoc.webmvc.api.WebMvcActuatorProvider;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.web.servlet.ControllerEndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;
import static org.springdoc.core.Constants.SPRINGDOC_USE_MANAGEMENT_PORT;

/**
 * The type Spring doc web mvc configuration.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@Import(OpenApiRouter.class)
public class SpringDocWebMvcConfiguration {

	/**
	 * Open api resource open api resource.
	 *
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param requestMappingHandlerMapping the request mapping handler mapping
	 * @param actuatorProvider the actuator provider
	 * @param springDocConfigProperties the spring doc config properties
	 * @param operationCustomizers the operation customizers
	 * @param openApiCustomisers the open api customisers
	 * @param springSecurityOAuth2Provider the spring security o auth 2 provider
	 * @param routerFunctionProvider the router function provider
	 * @param repositoryRestResourceProvider the repository rest resource provider
	 * @return the open api resource
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	@Lazy(false)
	OpenApiResource openApiResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
			Optional<ActuatorProvider> actuatorProvider,
			SpringDocConfigProperties springDocConfigProperties,
			Optional<List<OperationCustomizer>> operationCustomizers,
			Optional<List<OpenApiCustomiser>> openApiCustomisers,
			Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider,
			Optional<RouterFunctionProvider> routerFunctionProvider,
			Optional<RepositoryRestResourceProvider> repositoryRestResourceProvider) {
		return new OpenApiResource(openAPIBuilderObjectFactory, requestBuilder,
				responseBuilder, operationParser,
				requestMappingHandlerMapping, actuatorProvider, operationCustomizers,
				openApiCustomisers, springDocConfigProperties, springSecurityOAuth2Provider,
				routerFunctionProvider, repositoryRestResourceProvider);
	}

	/**
	 * Request builder request builder.
	 *
	 * @param parameterBuilder the parameter builder
	 * @param requestBodyService the request body builder
	 * @param operationService the operation builder
	 * @param parameterCustomizers the parameter customizers
	 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer
	 * @return the request builder
	 */
	@Bean
	@ConditionalOnMissingBean
	RequestService requestBuilder(GenericParameterService parameterBuilder, RequestBodyService requestBodyService,
			OperationService operationService, Optional<List<ParameterCustomizer>> parameterCustomizers,
			LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
		return new RequestService(parameterBuilder, requestBodyService,
				operationService, parameterCustomizers, localSpringDocParameterNameDiscoverer);
	}

	/**
	 * Response builder generic response builder.
	 *
	 * @param operationService the operation builder
	 * @param returnTypeParsers the return type parsers
	 * @param springDocConfigProperties the spring doc config properties
	 * @param propertyResolverUtils the property resolver utils
	 * @return the generic response builder
	 */
	@Bean
	@ConditionalOnMissingBean
	GenericResponseService responseBuilder(OperationService operationService, List<ReturnTypeParser> returnTypeParsers, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils) {
		return new GenericResponseService(operationService, returnTypeParsers, springDocConfigProperties, propertyResolverUtils);
	}

	/**
	 * The type Spring doc web mvc router configuration.
	 * @author bnasslahsen
	 */
	@ConditionalOnClass(RouterFunction.class)
	class SpringDocWebMvcRouterConfiguration {

		/**
		 * Router function provider router function provider.
		 *
		 * @param applicationContext the application context
		 * @return the router function provider
		 */
		@Bean
		@ConditionalOnMissingBean
		RouterFunctionProvider routerFunctionProvider(ApplicationContext applicationContext) {
			return new RouterFunctionProvider(applicationContext);
		}
	}

	/**
	 * The type Spring doc web mvc actuator configuration.
	 * @author bnasslahsen
	 */
	@ConditionalOnProperty(SPRINGDOC_SHOW_ACTUATOR)
	@ConditionalOnClass(WebMvcEndpointHandlerMapping.class)
	@ConditionalOnManagementPort(ManagementPortType.SAME)
	static class SpringDocWebMvcActuatorConfiguration {

		/**
		 * Actuator provider actuator provider.
		 *
		 * @param webMvcEndpointHandlerMapping the web mvc endpoint handler mapping
		 * @param controllerEndpointHandlerMapping the controller endpoint handler mapping
		 * @return the actuator provider
		 */
		@Bean
		@ConditionalOnMissingBean
		ActuatorProvider actuatorProvider(WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping, ControllerEndpointHandlerMapping controllerEndpointHandlerMapping) {
			return new WebMvcActuatorProvider(webMvcEndpointHandlerMapping, controllerEndpointHandlerMapping);
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
		 * Actuator customizer OpenAPI customiser.
		 *
		 * @return the OpenAPI customiser
		 */
		@Bean
		@Lazy(false)
		OpenApiCustomiser actuatorOpenApiCustomiser() {
			return new ActuatorOpenApiCustomizer();
		}
	}

	@ConditionalOnProperty(SPRINGDOC_USE_MANAGEMENT_PORT)
	@ConditionalOnClass(WebMvcEndpointHandlerMapping.class)
	@ConditionalOnManagementPort(ManagementPortType.DIFFERENT)
	static class SpringDocWebMvcActuatorDifferentConfiguration {

		@Bean
		@ConditionalOnMissingBean(MultipleOpenApiSupportConfiguration.class)
		@Lazy(false)
		OpenApiResource openApiActuatorResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
				GenericResponseService responseBuilder, OperationService operationParser,
				RequestMappingInfoHandlerMapping requestMappingHandlerMapping,
				Optional<ActuatorProvider> actuatorProvider,
				SpringDocConfigProperties springDocConfigProperties,
				Optional<List<OperationCustomizer>> operationCustomizers,
				Optional<List<OpenApiCustomiser>> openApiCustomisers,
				Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider,
				Optional<RouterFunctionProvider> routerFunctionProvider,
				Optional<RepositoryRestResourceProvider> repositoryRestResourceProvider,
				ServletWebServerApplicationContext serverApplicationContext) {
			return new OpenApiActuatorResource(openAPIBuilderObjectFactory, requestBuilder,
					responseBuilder, operationParser,
					requestMappingHandlerMapping, actuatorProvider, operationCustomizers,
					openApiCustomisers, springDocConfigProperties, springSecurityOAuth2Provider,
					routerFunctionProvider, repositoryRestResourceProvider,serverApplicationContext);
		}

	}

}
