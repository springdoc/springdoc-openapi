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

import org.springdoc.core.properties.SwaggerUiConfigProperties;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.resource.LiteWebJarsResourceResolver;

import static org.springdoc.ui.SwaggerResourceResolverUtils.findSwaggerResourcePath;

/**
 * The type Web jars version resource resolver.
 *
 * @author bnasslahsen
 */
public class SwaggerResourceResolver extends LiteWebJarsResourceResolver {

	/**
	 * The Swagger ui config properties.
	 */
	private final SwaggerUiConfigProperties swaggerUiConfigProperties;

	/**
	 * Instantiates a new Web jars version resource resolver.
	 *
	 * @param swaggerUiConfigProperties the swagger ui config properties
	 */
	public SwaggerResourceResolver(SwaggerUiConfigProperties swaggerUiConfigProperties) {
		this.swaggerUiConfigProperties = swaggerUiConfigProperties;
	}

	/**
	 * Find web jar resource path string.
	 *
	 * @param pathStr the path
	 * @return the string
	 */
	@Nullable
	@Override
	protected String findWebJarResourcePath(String pathStr) {
		String resourcePath = super.findWebJarResourcePath(pathStr);
		if(resourcePath == null)
			return findSwaggerResourcePath(pathStr, swaggerUiConfigProperties.getVersion());
		return resourcePath;
	}

}