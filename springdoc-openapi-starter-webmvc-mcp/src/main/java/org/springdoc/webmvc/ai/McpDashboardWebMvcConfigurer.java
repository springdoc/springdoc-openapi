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

package org.springdoc.webmvc.ai;

import org.springdoc.ai.properties.SpringDocAiProperties;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc configurer for serving the MCP Dashboard static resources.
 *
 * @author bnasslahsen
 */
public class McpDashboardWebMvcConfigurer implements WebMvcConfigurer {

	/**
	 * The AI properties.
	 */
	private final SpringDocAiProperties aiProperties;

	/**
	 * Constructs a new McpDashboardWebMvcConfigurer.
	 * @param aiProperties the AI properties
	 */
	public McpDashboardWebMvcConfigurer(SpringDocAiProperties aiProperties) {
		this.aiProperties = aiProperties;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dashboardPath = aiProperties.getDashboardPath();
		registry.addResourceHandler(dashboardPath + "/**").addResourceLocations("classpath:/mcp-ui/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		String dashboardPath = aiProperties.getDashboardPath();
		registry.addRedirectViewController(dashboardPath, dashboardPath + "/index.html");
		registry.addRedirectViewController(dashboardPath + "/", dashboardPath + "/index.html");
	}

}
