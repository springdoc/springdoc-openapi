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

package org.springdoc.webmvc.scalar;

import com.scalar.maven.webjar.ScalarProperties;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.events.SpringDocAppInitializer;
import org.springdoc.core.properties.SpringDocConfigProperties;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.webmvc.actuate.endpoint.web.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.filter.ForwardedHeaderFilter;

import static org.springdoc.core.utils.Constants.SCALAR_ENABLED;
import static org.springdoc.core.utils.Constants.SPRINGDOC_USE_MANAGEMENT_PORT;
import static org.springdoc.scalar.ScalarConstants.DEFAULT_SCALAR_ACTUATOR_PATH;

/**
 * The type Scalar configuration.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SCALAR_ENABLED, matchIfMissing = true)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnBean(SpringDocConfiguration.class)
@EnableConfigurationProperties(ScalarProperties.class)
public class ScalarConfiguration {

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
	ScalarWebMvcController scalarWebMvcController(ScalarProperties scalarProperties, SpringDocConfigProperties springDocConfigProperties) {
		return new ScalarWebMvcController(scalarProperties, springDocConfigProperties);
	}

	/**
	 * Forwarded header filter filter registration bean.
	 *
	 * @return the filter registration bean
	 */
	@Bean
	@ConditionalOnMissingBean
	@Lazy(false)
	public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
		return new FilterRegistrationBean<>(new ForwardedHeaderFilter());
	}

	/**
	 * Spring doc app initializer spring doc app initializer.
	 *
	 * @param scalarProperties the spring doc config properties
	 * @return the spring doc app initializer
	 */
	@Bean
	@ConditionalOnMissingBean(name = "springDocScalarInitializer")
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	@Lazy(false)
	SpringDocAppInitializer springDocScalarInitializer(ScalarProperties scalarProperties) {
		return new SpringDocAppInitializer(scalarProperties.getPath(), SCALAR_ENABLED, scalarProperties.isEnabled());
	}
	
	/**
	 * The type Swagger actuator welcome configuration.
	 */
	@ConditionalOnProperty(SPRINGDOC_USE_MANAGEMENT_PORT)
	@ConditionalOnClass(WebMvcEndpointHandlerMapping.class)
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
		ScalarActuatorController scalarActuatorController(ScalarProperties properties, WebEndpointProperties webEndpointProperties) {
			return new ScalarActuatorController(properties, webEndpointProperties);
		}
		
		
		/**
		 * Spring doc scalar initializer spring doc app initializer.
		 *
		 * @return the spring doc app initializer
		 */
		@Bean
		@ConditionalOnMissingBean(name = "springDocScalarInitializer")
		@Lazy(false)
		SpringDocAppInitializer springDocScalarInitializer(ScalarProperties properties) {
			return new SpringDocAppInitializer(DEFAULT_SCALAR_ACTUATOR_PATH, SCALAR_ENABLED, properties.isEnabled());
		}
	}

}
