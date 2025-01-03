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

package org.springdoc.webmvc.core.configuration;

import java.util.List;
import java.util.Optional;

import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.discoverer.SpringDocParameterNameDiscoverer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ActuatorProvider;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericParameterService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.core.service.RequestBodyService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springdoc.webmvc.api.OpenApiActuatorResource;
import org.springdoc.webmvc.api.OpenApiWebMvcResource;
import org.springdoc.webmvc.core.providers.ActuatorWebMvcProvider;
import org.springdoc.webmvc.core.providers.RouterFunctionWebMvcProvider;
import org.springdoc.webmvc.core.providers.SpringWebMvcProvider;
import org.springdoc.webmvc.core.service.RequestService;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springdoc.core.utils.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.utils.SpringDocUtils.getConfig;

/**
 * The type Spring doc web mvc configuration.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocWebMvcConfiguration {

	static {
		getConfig().setResponseEntityExceptionHandlerClass(ResponseEntityExceptionHandler.class)
				.setModelAndViewClass(ModelAndView.class);
	}

	/**
	 * Open api resource open api resource.
	 *
	 * @param openAPIBuilderObjectFactory the open api builder object factory
	 * @param requestBuilder              the request builder
	 * @param responseBuilder             the response builder
	 * @param operationParser             the operation parser
	 * @param springDocConfigProperties   the spring doc config properties
	 * @param springDocProviders          the spring doc providers
	 * @param springDocCustomizers        the spring doc customizers
	 * @return the open api resource
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnExpression("(${springdoc.use-management-port:false} == false ) and ${springdoc.enable-default-api-docs:true}")
	@Lazy(false)
	OpenApiWebMvcResource openApiResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			SpringDocConfigProperties springDocConfigProperties,
			SpringDocProviders springDocProviders, SpringDocCustomizers springDocCustomizers) {
		return new OpenApiWebMvcResource(openAPIBuilderObjectFactory, requestBuilder,
				responseBuilder, operationParser,  springDocConfigProperties, springDocProviders, springDocCustomizers);
	}

	/**
	 * Request builder request builder.
	 *
	 * @param parameterBuilder                      the parameter builder
	 * @param requestBodyService                    the request body builder
	 * @param parameterCustomizers                  the parameter customizers
	 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer
	 * @return the request builder
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	RequestService requestBuilder(GenericParameterService parameterBuilder, RequestBodyService requestBodyService,
			Optional<List<ParameterCustomizer>> parameterCustomizers,
			SpringDocParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
		return new RequestService(parameterBuilder, requestBodyService,
				parameterCustomizers, localSpringDocParameterNameDiscoverer);
	}

	/**
	 * Spring web provider spring web provider.
	 *
	 * @return the spring web provider
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	SpringWebProvider springWebProvider() {
		return new SpringWebMvcProvider();
	}

	/**
	 * Response builder generic response builder.
	 *
	 * @param operationService          the operation builder
	 * @param springDocConfigProperties the spring doc config properties
	 * @param propertyResolverUtils     the property resolver utils
	 * @return the generic response builder
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	GenericResponseService responseBuilder(OperationService operationService, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils) {
		return new GenericResponseService(operationService, springDocConfigProperties, propertyResolverUtils);
	}

	/**
	 * The type Spring doc web mvc router configuration.
	 *
	 * @author bnasslahsen
	 */
	@ConditionalOnClass(RouterFunction.class)
	static class SpringDocWebMvcRouterConfiguration {

		/**
		 * Router function provider router function provider.
		 *
		 * @return the router function provider
		 */
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		RouterFunctionWebMvcProvider routerFunctionProvider() {
			return new RouterFunctionWebMvcProvider();
		}
	}

	/**
	 * The type Spring doc web mvc actuator configuration.
	 *
	 * @author bnasslashen
	 */
	@ConditionalOnClass(WebMvcEndpointHandlerMapping.class)
	static class SpringDocWebMvcActuatorConfiguration {

		/**
		 * Actuator provider actuator provider.
		 *
		 * @param serverProperties                 the server properties
		 * @param springDocConfigProperties        the spring doc config properties
		 * @param managementServerProperties       the management server properties
		 * @param webEndpointProperties            the web endpoint properties
		 * @return the actuator provider
		 */
		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnExpression("${springdoc.show-actuator:false} or ${springdoc.use-management-port:false}")
		@Lazy(false)
		ActuatorProvider actuatorProvider(ServerProperties serverProperties,
				SpringDocConfigProperties springDocConfigProperties,
				Optional<ManagementServerProperties> managementServerProperties,
				Optional<WebEndpointProperties> webEndpointProperties) {
			return new ActuatorWebMvcProvider(serverProperties,
					springDocConfigProperties,
					managementServerProperties,
					webEndpointProperties);
		}

		/**
		 * Open api actuator resource open api actuator resource.
		 *
		 * @param openAPIBuilderObjectFactory the open api builder object factory
		 * @param requestBuilder              the request builder
		 * @param responseBuilder             the response builder
		 * @param operationParser             the operation parser
		 * @param springDocConfigProperties   the spring doc config properties
		 * @param springDocProviders          the spring doc providers
		 * @param springDocCustomizers        the spring doc customizers
		 * @return the open api actuator resource
		 */
		@Bean
		@ConditionalOnMissingBean(MultipleOpenApiSupportConfiguration.class)
		@ConditionalOnExpression("${springdoc.use-management-port:false} and ${springdoc.enable-default-api-docs:true}")
		@ConditionalOnManagementPort(ManagementPortType.DIFFERENT)
		@Lazy(false)
		OpenApiActuatorResource openApiActuatorResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
				GenericResponseService responseBuilder, OperationService operationParser,
				SpringDocConfigProperties springDocConfigProperties,
				SpringDocProviders springDocProviders,  SpringDocCustomizers springDocCustomizers) {
			return new OpenApiActuatorResource(openAPIBuilderObjectFactory,
					requestBuilder, responseBuilder,
					operationParser,
					springDocConfigProperties, springDocProviders,  springDocCustomizers);
		}
	}
}
