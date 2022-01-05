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

package org.springdoc.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.Constants;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;

import org.springframework.util.CollectionUtils;

import static org.springdoc.core.SwaggerUiConfigParameters.QUERY_CONFIG_ENABLED_PROPERTY;

/**
 * The type Abstract swagger index transformer.
 * @author bnasslahsen
 */
public class AbstractSwaggerIndexTransformer {

	/**
	 * The constant PRESETS.
	 */
	private static final String PRESETS = "presets: [";

	/**
	 * The Swagger ui o auth properties.
	 */
	protected SwaggerUiOAuthProperties swaggerUiOAuthProperties;

	/**
	 * The Swagger ui config parameters.
	 */
	protected SwaggerUiConfigParameters swaggerUiConfigParameters;

	/**
	 * The Object mapper.
	 */
	protected ObjectMapper objectMapper;

	/**
	 * The Swagger ui config.
	 */
	protected SwaggerUiConfigProperties swaggerUiConfig;

	/**
	 * Instantiates a new Abstract swagger index transformer.
	 *
	 * @param swaggerUiConfig the swagger ui config
	 * @param swaggerUiOAuthProperties the swagger ui o auth properties
	 * @param swaggerUiConfigParameters the swagger ui config parameters
	 */
	public AbstractSwaggerIndexTransformer(SwaggerUiConfigProperties swaggerUiConfig, SwaggerUiOAuthProperties swaggerUiOAuthProperties, SwaggerUiConfigParameters swaggerUiConfigParameters) {
		this.swaggerUiConfig = swaggerUiConfig;
		this.swaggerUiOAuthProperties = swaggerUiOAuthProperties;
		this.swaggerUiConfigParameters = swaggerUiConfigParameters;
		this.objectMapper = Json.mapper();
	}

	/**
	 * Add init oauth string.
	 *
	 * @param html the html
	 * @return the string
	 * @throws JsonProcessingException the json processing exception
	 */
	protected String addInitOauth(String html) throws JsonProcessingException {
		StringBuilder stringBuilder = new StringBuilder("window.ui = ui\n");
		stringBuilder.append("ui.initOAuth(\n");
		String json = objectMapper.writeValueAsString(swaggerUiOAuthProperties.getConfigParameters());
		stringBuilder.append(json);
		stringBuilder.append(")");
		return html.replace("window.ui = ui", stringBuilder.toString());
	}

	/**
	 * Read fully as string string.
	 *
	 * @param inputStream the input stream
	 * @return the string
	 * @throws IOException the io exception
	 */
	protected String readFullyAsString(InputStream inputStream)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toString(StandardCharsets.UTF_8.name());
	}

	/**
	 * Overwrite swagger default url string.
	 *
	 * @param html the html
	 * @return the string
	 */
	protected String overwriteSwaggerDefaultUrl(String html) {
		return html.replace(Constants.SWAGGER_UI_DEFAULT_URL, StringUtils.EMPTY);
	}

	/**
	 * Default transformations string.
	 *
	 * @param inputStream the input stream
	 * @return the string
	 * @throws IOException the io exception
	 */
	protected String defaultTransformations(InputStream inputStream) throws IOException {
		String html = readFullyAsString(inputStream);
		if (!CollectionUtils.isEmpty(swaggerUiOAuthProperties.getConfigParameters()))
			html = addInitOauth(html);

		if (swaggerUiConfig.isCsrfEnabled()) {
			if (swaggerUiConfig.getCsrf().isUseLocalStorage())
				html = addCSRFLocalStorage(html);
			else
				html = addCSRF(html);
		}

		if (swaggerUiConfig.getSyntaxHighlight().isPresent())
			html = addSyntaxHighlight(html);

		if (swaggerUiConfig.getQueryConfigEnabled() == null || !swaggerUiConfig.getQueryConfigEnabled())
			html = addParameters(html);
		else
			html = addParameter(html, QUERY_CONFIG_ENABLED_PROPERTY, swaggerUiConfig.getQueryConfigEnabled().toString());

		if (swaggerUiConfig.isDisableSwaggerDefaultUrl())
			html = overwriteSwaggerDefaultUrl(html);

		return html;
	}

	protected String addParameters(String html) throws JsonProcessingException {
		String layout = swaggerUiConfigParameters.getLayout() != null ? swaggerUiConfigParameters.getLayout() : "StandaloneLayout";
		StringBuilder stringBuilder = new StringBuilder("layout: \"" + layout + "\" ,\n");

		Map<String, Object> parametersObjectMap = swaggerUiConfigParameters.getConfigParameters().entrySet().stream()
				.filter(entry -> !SwaggerUiConfigParameters.OAUTH2_REDIRECT_URL_PROPERTY.equals(entry.getKey()))
				.filter(entry -> !SwaggerUiConfigParameters.URL_PROPERTY.equals(entry.getKey()))
				.filter(entry -> !SwaggerUiConfigParameters.URLS_PROPERTY.equals(entry.getKey()))
				.filter(entry -> SwaggerUiConfigParameters.VALIDATOR_URL_PROPERTY.equals(entry.getKey()) || StringUtils.isNotEmpty((String) entry.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1, e2) -> e2,
						LinkedHashMap::new));

		if (!CollectionUtils.isEmpty(parametersObjectMap)) {
			String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(parametersObjectMap);
			result = result.substring(1, result.length() - 1);
			stringBuilder.append(result);
			html = html.replace("layout: \"StandaloneLayout\"", stringBuilder.toString());
		}

		return html;
	}

	private String addParameter(String html, String key, String value) {
		StringBuilder stringBuilder = new StringBuilder("const ui = SwaggerUIBundle({\n");
		stringBuilder.append(key + ": \"" + value + "\",");
		return html.replace("const ui = SwaggerUIBundle({", stringBuilder.toString());
	}

	/**
	 * Add csrf string.
	 *
	 * @param html the html
	 * @return the string
	 */
	protected String addCSRF(String html) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("requestInterceptor: (request) => {\n");
		stringBuilder.append("const value = `; ${document.cookie}`;\n");
		stringBuilder.append("const parts = value.split(`; ");
		stringBuilder.append(swaggerUiConfig.getCsrf().getCookieName());
		stringBuilder.append("=`);\n");
		stringBuilder.append("if (parts.length === 2)\n");
		stringBuilder.append("request.headers['");
		stringBuilder.append(swaggerUiConfig.getCsrf().getHeaderName());
		stringBuilder.append("'] = parts.pop().split(';').shift();\n");
		stringBuilder.append("return request;\n");
		stringBuilder.append("},\n");
		stringBuilder.append(PRESETS);
		return html.replace(PRESETS, stringBuilder.toString());
	}

	/**
	 * Add csrf string.
	 *
	 * @param html the html
	 * @return the string
	 */
	protected String addCSRFLocalStorage(String html) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("requestInterceptor: (request) => {\n");
		stringBuilder.append("const value = window.localStorage.getItem('");
		stringBuilder.append(swaggerUiConfig.getCsrf().getLocalStorageKey() + "');\n");
		stringBuilder.append("request.headers['");
		stringBuilder.append(swaggerUiConfig.getCsrf().getHeaderName());
		stringBuilder.append("'] = value;\n");
		stringBuilder.append("return request;\n");
		stringBuilder.append("},\n");
		stringBuilder.append(PRESETS);
		return html.replace(PRESETS, stringBuilder.toString());
	}

	/**
	 * Add syntax highlight string.
	 *
	 * @param html the html
	 * @return the string
	 */
	protected String addSyntaxHighlight(String html) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("syntaxHighlight: {");
		if (swaggerUiConfig.getSyntaxHighlight().getActivated() != null) {
			stringBuilder.append("activated: ");
			stringBuilder.append(swaggerUiConfig.getSyntaxHighlight().getActivated());
		}
		if (StringUtils.isNotEmpty(swaggerUiConfig.getSyntaxHighlight().getTheme())) {
			if (swaggerUiConfig.getSyntaxHighlight().getActivated() != null)
				stringBuilder.append(" , ");
			stringBuilder.append("theme: \"");
			stringBuilder.append(swaggerUiConfig.getSyntaxHighlight().getTheme());
			stringBuilder.append("\"");
		}
		stringBuilder.append("},\n");
		stringBuilder.append(PRESETS);
		return html.replace(PRESETS, stringBuilder.toString());
	}

}
