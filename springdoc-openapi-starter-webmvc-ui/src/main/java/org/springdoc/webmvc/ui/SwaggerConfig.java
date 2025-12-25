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

package org.springdoc.webmvc.ui;

import java.util.Optional;

import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.events.SpringDocAppInitializer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.webmvc.core.providers.SpringWebMvcProvider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.webmvc.actuate.endpoint.web.WebMvcEndpointHandlerMapping;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.accept.ApiVersionStrategy;

import static org.springdoc.core.utils.Constants.DEFAULT_SWAGGER_UI_ACTUATOR_PATH;
import static org.springdoc.core.utils.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static org.springdoc.core.utils.Constants.SPRINGDOC_USE_MANAGEMENT_PORT;
import static org.springdoc.core.utils.Constants.SPRINGDOC_USE_ROOT_PATH;


/**
 * The type Swagger config.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerConfig {

	/**
	 * Swagger welcome swagger welcome web mvc.
	 *
	 * @param swaggerUiConfig           the swagger ui config
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springWebProviderObjectProvider         the spring web provider
	 * @return the swagger welcome web mvc
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	@Lazy(false)
	SwaggerWelcomeWebMvc swaggerWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, ObjectProvider<SpringWebProvider> springWebProviderObjectProvider) {
		return new SwaggerWelcomeWebMvc(swaggerUiConfig, springDocConfigProperties, springWebProviderObjectProvider);
	}

	/**
	 * Spring web provider spring web provider.
	 *
	 * @param apiVersionStrategyOptional the api version strategy optional
	 * @return the spring web provider
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	SpringWebProvider springWebProvider(Optional<ApiVersionStrategy> apiVersionStrategyOptional) {
		return new SpringWebMvcProvider(apiVersionStrategyOptional);
	}

	/**
	 * Swagger config resource swagger config resource.
	 *
	 * @param swaggerWelcomeCommon the swagger welcome common
	 * @return the swagger config resource
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	@Lazy(false)
	SwaggerConfigResource swaggerConfigResource(SwaggerWelcomeCommon swaggerWelcomeCommon) {
		return new SwaggerConfigResource(swaggerWelcomeCommon);
	}

	/**
	 * Swagger ui home swagger ui home.
	 *
	 * @return the swagger ui home
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = SPRINGDOC_USE_ROOT_PATH, havingValue = "true")
	@Lazy(false)
	SwaggerUiHome swaggerUiHome() {
		return new SwaggerUiHome();
	}

	/**
	 * Index page transformer swagger index transformer.
	 *
	 * @param swaggerUiConfig          the swagger ui config
	 * @param swaggerUiOAuthProperties the swagger ui o auth properties
	 * @param swaggerWelcomeCommon     the swagger welcome common
	 * @param objectMapperProvider     the object mapper provider
	 * @return the swagger index transformer
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	SwaggerIndexTransformer indexPageTransformer(SwaggerUiConfigProperties swaggerUiConfig, SwaggerUiOAuthProperties swaggerUiOAuthProperties,
			SwaggerWelcomeCommon swaggerWelcomeCommon, ObjectMapperProvider objectMapperProvider) {
		return new SwaggerIndexPageTransformer(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider);
	}

	/**
	 * Swagger web mvc configurer swagger web mvc configurer.
	 *
	 * @param swaggerUiConfigProperties the swagger ui calculated config
	 * @param springWebProperties       the spring web config
	 * @param springWebMvcProperties    the spring mvc config
	 * @param swaggerIndexTransformer   the swagger index transformer
	 * @param swaggerResourceResolver   the swagger resource resolver
	 * @param swaggerWelcomeCommon   the swagger welcome common
	 * @return the swagger web mvc configurer
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	SwaggerWebMvcConfigurer swaggerWebMvcConfigurer(SwaggerUiConfigProperties swaggerUiConfigProperties,
			WebProperties springWebProperties, WebMvcProperties springWebMvcProperties,
			SwaggerIndexTransformer swaggerIndexTransformer, SwaggerResourceResolver swaggerResourceResolver,
			SwaggerWelcomeCommon swaggerWelcomeCommon) {
		return new SwaggerWebMvcConfigurer(swaggerUiConfigProperties, springWebProperties, springWebMvcProperties, swaggerIndexTransformer, swaggerResourceResolver, swaggerWelcomeCommon);
	}

	/**
	 * Swagger resource resolver swagger resource resolver.
	 *
	 * @param swaggerUiConfigProperties the swagger ui config properties
	 * @return the swagger resource resolver
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	SwaggerResourceResolver swaggerResourceResolver(SwaggerUiConfigProperties swaggerUiConfigProperties) {
		return new SwaggerResourceResolver(swaggerUiConfigProperties);
	}

	/**
	 * Spring doc swagger initializer spring doc swagger initializer.
	 *
	 * @param swaggerUiConfigProperties the swagger ui config properties
	 * @return the spring doc swagger initializer
	 */
	@Bean
	@ConditionalOnMissingBean(name = "springDocSwaggerInitializer")
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	@Lazy(false)
	SpringDocAppInitializer springDocSwaggerInitializer(SwaggerUiConfigProperties swaggerUiConfigProperties) {
		return new SpringDocAppInitializer(swaggerUiConfigProperties.getPath(), SPRINGDOC_SWAGGER_UI_ENABLED, swaggerUiConfigProperties.isEnabled());
	}
	
	/**
	 * The type Swagger actuator welcome configuration.
	 */
	@ConditionalOnProperty(SPRINGDOC_USE_MANAGEMENT_PORT)
	@ConditionalOnClass(WebMvcEndpointHandlerMapping.class)
	@ConditionalOnManagementPort(ManagementPortType.DIFFERENT)
	static class SwaggerActuatorWelcomeConfiguration {

		/**
		 * Swagger actuator welcome swagger welcome actuator.
		 *
		 * @param swaggerUiConfig           the swagger ui config
		 * @param springDocConfigProperties the spring doc config properties
		 * @param webEndpointProperties     the web endpoint properties
		 * @return the swagger welcome actuator
		 */
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		SwaggerWelcomeActuator swaggerActuatorWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, WebEndpointProperties webEndpointProperties) {
			return new SwaggerWelcomeActuator(swaggerUiConfig, springDocConfigProperties, webEndpointProperties);
		}
		
		/**
		 * Spring doc swagger initializer spring doc app initializer.
		 *
		 * @return the spring doc app initializer
		 */
		@Bean
		@ConditionalOnMissingBean(name = "springDocSwaggerInitializer")
		@Lazy(false)
		SpringDocAppInitializer springDocSwaggerInitializer(SwaggerUiConfigProperties swaggerUiConfigProperties) {
			return new SpringDocAppInitializer(DEFAULT_SWAGGER_UI_ACTUATOR_PATH, SPRINGDOC_SWAGGER_UI_ENABLED, swaggerUiConfigProperties.isEnabled());
		}
	}
}
