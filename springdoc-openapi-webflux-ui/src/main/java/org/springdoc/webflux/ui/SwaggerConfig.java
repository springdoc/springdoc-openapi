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

package org.springdoc.webflux.ui;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.actuate.endpoint.web.reactive.WebFluxEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static org.springdoc.core.Constants.SPRINGDOC_USE_MANAGEMENT_PORT;


/**
 * The type Swagger config.
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerConfig implements WebFluxConfigurer {

	/**
	 * Swagger welcome swagger welcome web flux.
	 *
	 * @param swaggerUiConfig the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 * @param requestMappingHandlerMapping the request mapping handler mapping
	 * @return the swagger welcome web flux
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	SwaggerWelcomeWebFlux swaggerWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties,SwaggerUiConfigParameters swaggerUiConfigParameters, RequestMappingInfoHandlerMapping requestMappingHandlerMapping) {
		return new SwaggerWelcomeWebFlux(swaggerUiConfig,springDocConfigProperties,swaggerUiConfigParameters,requestMappingHandlerMapping);
	}

	/**
	 * Swagger web flux configurer swagger web flux configurer.
	 *
	 * @param swaggerUiConfigParameters the swagger ui calculated config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param swaggerIndexTransformer the swagger index transformer
	 * @param actuatorProvider the actuator provider
	 * @return the swagger web flux configurer
	 */
	@Bean
	@ConditionalOnMissingBean
	SwaggerWebFluxConfigurer swaggerWebFluxConfigurer(SwaggerUiConfigParameters swaggerUiConfigParameters, SpringDocConfigProperties springDocConfigProperties, SwaggerIndexTransformer swaggerIndexTransformer, Optional<ActuatorProvider> actuatorProvider) {
		return new SwaggerWebFluxConfigurer(swaggerUiConfigParameters, springDocConfigProperties, swaggerIndexTransformer,actuatorProvider);
	}

	/**
	 * Index page transformer swagger index transformer.
	 *
	 * @param swaggerUiConfig the swagger ui config
	 * @param swaggerUiOAuthProperties the swagger ui o auth properties
	 * @param objectMapper the object mapper
	 * @return the swagger index transformer
	 */
	@Bean
	@ConditionalOnMissingBean
	SwaggerIndexTransformer indexPageTransformer(SwaggerUiConfigProperties swaggerUiConfig ,SwaggerUiOAuthProperties swaggerUiOAuthProperties, ObjectMapper objectMapper) {
		return new SwaggerIndexPageTransformer(swaggerUiConfig, swaggerUiOAuthProperties, objectMapper);
	}

	/**
	 * Swagger ui config parameters swagger ui config parameters.
	 *
	 * @param swaggerUiConfig the swagger ui config
	 * @return the swagger ui config parameters
	 */
	@Bean
	@ConditionalOnMissingBean
	SwaggerUiConfigParameters swaggerUiConfigParameters (SwaggerUiConfigProperties swaggerUiConfig){
		return new SwaggerUiConfigParameters(swaggerUiConfig);
	}

	/**
	 * The type Swagger actuator welcome configuration.
	 * @author bnasslashen
	 */
	@ConditionalOnProperty(SPRINGDOC_USE_MANAGEMENT_PORT)
	@ConditionalOnClass(WebFluxEndpointHandlerMapping.class)
	@ConditionalOnManagementPort(ManagementPortType.DIFFERENT)
	static class SwaggerActuatorWelcomeConfiguration {

		/**
		 * Swagger actuator welcome swagger welcome actuator.
		 *
		 * @param swaggerUiConfig the swagger ui config
		 * @param springDocConfigProperties the spring doc config properties
		 * @param swaggerUiConfigParameters the swagger ui config parameters
		 * @param webEndpointProperties the web endpoint properties
		 * @param managementServerProperties the management server properties
		 * @return the swagger welcome actuator
		 */
		@Bean
		@ConditionalOnMissingBean
		SwaggerWelcomeActuator swaggerActuatorWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties,
				SwaggerUiConfigParameters swaggerUiConfigParameters, WebEndpointProperties webEndpointProperties, ManagementServerProperties managementServerProperties) {
			return new SwaggerWelcomeActuator(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters, webEndpointProperties,managementServerProperties);
		}
	}
}
