/*
 *
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
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

import org.springframework.util.CollectionUtils;

/**
 * The type Actuator open api customiser.
 * @author bnasslahsen
 */
public class ActuatorOpenApiCustomizer implements OpenApiCustomiser {

	/**
	 * The Path pathern.
	 */
	private final Pattern pathPathern = Pattern.compile("\\{(.*?)}");

	@Override
	public void customise(OpenAPI openApi) {
		openApi.getPaths().entrySet().stream()
				.filter(stringPathItemEntry -> stringPathItemEntry.getKey().startsWith("/actuator/"))
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
