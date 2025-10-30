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

package org.springdoc.webflux.scalar;

import com.scalar.maven.webjar.ScalarProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.web.reactive.WebFluxEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

import static org.springdoc.core.utils.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static org.springdoc.core.utils.Constants.SPRINGDOC_USE_MANAGEMENT_PORT;

/**
 * The type Scalar configuration.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnWebApplication(type = Type.REACTIVE)
@ConditionalOnBean(SpringDocConfiguration.class)
@EnableConfigurationProperties(ScalarProperties.class)
@ConditionalOnProperty(prefix = "scalar", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ScalarConfiguration {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ScalarConfiguration.class);

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		LOGGER.warn("SpringDoc Scalar is enabled by default. To disable it in production, set the property 'scalar.enabled=false' in your production profile configuration.");
	}


	/**
	 * Scalar web mvc controller scalar web mvc controller.
	 *
	 * @param scalarProperties          the scalar properties
	 * @param springDocConfigProperties the spring doc config properties
	 * @return the scalar web mvc controller
	 */
	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	@ConditionalOnMissingBean
	@Lazy(false)
	ScalarWebFluxController scalarWebMvcController(ScalarProperties scalarProperties, SpringDocConfigProperties springDocConfigProperties) {
		return new ScalarWebFluxController(scalarProperties,springDocConfigProperties);
	}

	/**
	 * Forwarded header transformer forwarded header transformer.
	 *
	 * @return the forwarded header transformer
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	ForwardedHeaderTransformer forwardedHeaderTransformer() {
		return new ForwardedHeaderTransformer();
	}

	/**
	 * The type Swagger actuator welcome configuration.
	 */
	@ConditionalOnProperty(SPRINGDOC_USE_MANAGEMENT_PORT)
	@ConditionalOnClass(WebFluxEndpointHandlerMapping.class)
	@ConditionalOnManagementPort(ManagementPortType.DIFFERENT)
	static class SwaggerActuatorWelcomeConfiguration {

		/**
		 * Scalar actuator controller scalar actuator controller.
		 *
		 * @param properties            the properties
		 * @param webEndpointProperties the web endpoint properties
		 * @return the scalar actuator controller
		 */
		@Bean
		@ConditionalOnMissingBean
		@Lazy(false)
		ScalarActuatorController scalarActuatorController(ScalarProperties properties,  WebEndpointProperties webEndpointProperties) {
			return new ScalarActuatorController(properties,webEndpointProperties);
		}
	}

}
