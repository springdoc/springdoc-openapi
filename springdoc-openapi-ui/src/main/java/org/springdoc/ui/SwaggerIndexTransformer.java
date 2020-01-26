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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springdoc.core.SwaggerUiOAuthProperties;

import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

public class SwaggerIndexTransformer implements ResourceTransformer {

	private SwaggerUiOAuthProperties swaggerUiOAuthProperties;

	private ObjectMapper objectMapper;

	public SwaggerIndexTransformer(SwaggerUiOAuthProperties swaggerUiOAuthProperties, ObjectMapper objectMapper) {
		this.swaggerUiOAuthProperties = swaggerUiOAuthProperties;
		this.objectMapper = objectMapper;
	}

	@Override
	public Resource transform(HttpServletRequest request, Resource resource,
			ResourceTransformerChain transformerChain) throws IOException {
		final AntPathMatcher antPathMatcher = new AntPathMatcher();
		boolean isIndexFound = antPathMatcher.match("**/swagger-ui/**/index.html", resource.getURL().toString());
		if (isIndexFound && !CollectionUtils.isEmpty(swaggerUiOAuthProperties.getConfigParameters())) {
			String html = readFullyAsString(resource.getInputStream());
			html = addInitOauth(html);
			return new TransformedResource(resource, html.getBytes());
		}
		else {
			return resource;
		}
	}

	private String addInitOauth(String html) throws JsonProcessingException {
		StringBuilder stringBuilder = new StringBuilder("window.ui = ui\n");
		stringBuilder.append("ui.initOAuth(\n");
		String json = objectMapper.writeValueAsString(swaggerUiOAuthProperties.getConfigParameters());
		stringBuilder.append(json);
		stringBuilder.append(")");
		return html.replace("window.ui = ui", stringBuilder.toString());
	}

	private String readFullyAsString(InputStream inputStream)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toString(StandardCharsets.UTF_8.name());
	}
}