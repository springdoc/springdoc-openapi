/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package org.springdoc.core;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.AntPathMatcher;

/**
 * The type Spring doc Native Configuration.
 * @author bnasslahsen
 */
@Lazy(false)
@ConditionalOnExpression("${springdoc.api-docs.enabled:true}")
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocUIConfiguration implements InitializingBean {

	/**
	 * The constant SPRINGDOC_CONFIG_PROPERTIES.
	 */
	public static final String SPRINGDOC_CONFIG_PROPERTIES = "springdoc.config.properties";

	/**
	 * The constant SPRINGDOC_SWAGGERUI_VERSION.
	 */
	private static final String SPRINGDOC_SWAGGER_UI_VERSION = "springdoc.swagger-ui.version";

	/**
	 * The Swagger ui config properties.
	 */
	private final Optional<SwaggerUiConfigProperties> optionalSwaggerUiConfigProperties;

	/**
	 * Instantiates a new Spring doc hints.
	 *
	 * @param optionalSwaggerUiConfigProperties the swagger ui config properties
	 */
	public SpringDocUIConfiguration(Optional<SwaggerUiConfigProperties> optionalSwaggerUiConfigProperties) {
		this.optionalSwaggerUiConfigProperties = optionalSwaggerUiConfigProperties;
	}

	@Override
	public void afterPropertiesSet() {
		optionalSwaggerUiConfigProperties.ifPresent(swaggerUiConfigProperties -> {
			if (StringUtils.isEmpty(swaggerUiConfigProperties.getVersion())) {
				try {
					Resource resource = new ClassPathResource(AntPathMatcher.DEFAULT_PATH_SEPARATOR + SPRINGDOC_CONFIG_PROPERTIES);
					Properties props = PropertiesLoaderUtils.loadProperties(resource);
					swaggerUiConfigProperties.setVersion(props.getProperty(SPRINGDOC_SWAGGER_UI_VERSION));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
}

