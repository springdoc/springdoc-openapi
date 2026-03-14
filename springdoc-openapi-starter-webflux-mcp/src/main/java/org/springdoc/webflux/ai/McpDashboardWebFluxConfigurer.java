/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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

package org.springdoc.webflux.ai;

import org.springdoc.ai.properties.SpringDocAiProperties;

import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * WebFlux configurer for serving the MCP Dashboard static resources.
 *
 * @author bnasslahsen
 */
public class McpDashboardWebFluxConfigurer implements WebFluxConfigurer {

	/**
	 * The AI properties.
	 */
	private final SpringDocAiProperties aiProperties;

	/**
	 * Constructs a new McpDashboardWebFluxConfigurer.
	 * @param aiProperties the AI properties
	 */
	public McpDashboardWebFluxConfigurer(SpringDocAiProperties aiProperties) {
		this.aiProperties = aiProperties;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dashboardPath = aiProperties.getDashboardPath();
		registry.addResourceHandler(dashboardPath + "/**").addResourceLocations("classpath:/mcp-ui/");
	}

}
