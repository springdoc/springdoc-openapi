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

package org.springdoc.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.springdoc.core.Constants.CLASSPATH_RESOURCE_LOCATION;
import static org.springdoc.core.Constants.DEFAULT_WEB_JARS_PREFIX_URL;
import static org.springdoc.core.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static org.springdoc.core.Constants.SWAGGER_UI_PATH;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;


@Configuration
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
@SuppressWarnings("deprecation")
public class SwaggerConfig extends WebMvcConfigurerAdapter { // NOSONAR

	@Value(SWAGGER_UI_PATH)
	private String swaggerPath;

	@Autowired
	private SwaggerIndexTransformer swaggerIndexTransformer;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		StringBuilder uiRootPath = new StringBuilder();
		if (swaggerPath.contains("/"))
			uiRootPath.append(swaggerPath, 0, swaggerPath.lastIndexOf('/'));
		uiRootPath.append("/**");
		registry.addResourceHandler(uiRootPath + "/swagger-ui/**")
				.addResourceLocations(CLASSPATH_RESOURCE_LOCATION + DEFAULT_WEB_JARS_PREFIX_URL + DEFAULT_PATH_SEPARATOR)
				.resourceChain(false)
				.addTransformer(swaggerIndexTransformer);
	}

	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
	SwaggerWelcome swaggerWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties) {
		return new SwaggerWelcome(swaggerUiConfig, springDocConfigProperties);
	}

	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
	SwaggerIndexTransformer indexPageTransformer(SwaggerUiOAuthProperties swaggerUiOAuthProperties, ObjectMapper objectMapper) {
		return new SwaggerIndexTransformer(swaggerUiOAuthProperties, objectMapper);
	}
}
