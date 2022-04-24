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

package org.springdoc.core.customizers;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.util.CollectionUtils;

import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Actuator open api customiser.
 * @author bnasslahsen
 */
public class ActuatorOpenApiCustomizer implements OpenApiCustomizer {

	/**
	 * The Path pathern.
	 */
	private final Pattern pathPathern = Pattern.compile("\\{(.*?)}");

	private final WebEndpointProperties webEndpointProperties;

	/**
	 * Instantiates a new Actuator open api customizer.
	 *
	 * @param webEndpointProperties the web endpoint properties
	 */
	public ActuatorOpenApiCustomizer(WebEndpointProperties webEndpointProperties) {
		this.webEndpointProperties = webEndpointProperties;
	}

	@Override
	public void customise(OpenAPI openApi) {
		if (!CollectionUtils.isEmpty(openApi.getPaths()))
			openApi.getPaths().entrySet().stream()
					.filter(stringPathItemEntry -> stringPathItemEntry.getKey().startsWith(webEndpointProperties.getBasePath() + DEFAULT_PATH_SEPARATOR))
					.forEach(stringPathItemEntry -> {
						String path = stringPathItemEntry.getKey();
						Matcher matcher = pathPathern.matcher(path);
						while (matcher.find()) {
							String pathParam = matcher.group(1);
							PathItem pathItem = stringPathItemEntry.getValue();
							pathItem.readOperations().forEach(operation -> {
								List<Parameter> existingParameters = operation.getParameters();
								Optional<Parameter> existingParam = Optional.empty();
								if (!CollectionUtils.isEmpty(existingParameters))
									existingParam = existingParameters.stream().filter(p -> pathParam.equals(p.getName())).findAny();
								if (!existingParam.isPresent())
									operation.addParametersItem(new PathParameter().name(pathParam).schema(new StringSchema()));
							});
						}
					});
	}
}
