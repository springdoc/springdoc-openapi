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

package org.springdoc.core.customizers;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;

/**
 * The type Actuator open api customizer.
 *
 * @author bnasslahsen
 * @since 2.7.0
 * @deprecated as not anymore required, use your own {@link org.springdoc.core.customizers.GlobalOpenApiCustomizer} instead
 */
@Deprecated(since = "2.7.0")
public class ActuatorOpenApiCustomizer implements GlobalOpenApiCustomizer {


	/**
	 * The Web endpoint properties.
	 */
	private final WebEndpointProperties webEndpointProperties;

	/**
	 * Instantiates a new Actuator open api customizer.
	 *
	 * @param webEndpointProperties the web endpoint properties
	 */
	public ActuatorOpenApiCustomizer(WebEndpointProperties webEndpointProperties) {
		this.webEndpointProperties = webEndpointProperties;
	}

	/**
	 * Actuator path entry stream stream.
	 *
	 * @param openApi         the open api
	 * @param relativeSubPath the relative sub path
	 * @return the stream
	 */
	private Stream<Entry<String, PathItem>> actuatorPathEntryStream(OpenAPI openApi, String relativeSubPath) {
		String pathPrefix = webEndpointProperties.getBasePath() + Optional.ofNullable(relativeSubPath).orElse("");
		return Optional.ofNullable(openApi.getPaths())
				.map(Paths::entrySet)
				.map(Set::stream)
				.map(s -> s.filter(entry -> entry.getKey().startsWith(pathPrefix)))
				.orElse(Stream.empty());
	}

	/**
	 * Handle actuator operation id uniqueness.
	 *
	 * @param openApi the open api
	 */
	private void handleActuatorOperationIdUniqueness(OpenAPI openApi) {
		Set<String> usedOperationIds = new HashSet<>();
		actuatorPathEntryStream(openApi, null)
				.sorted(Comparator.comparing(Entry::getKey))
				.forEachOrdered(stringPathItemEntry -> 
					stringPathItemEntry.getValue().readOperations().forEach(operation -> {
						String initialOperationId = operation.getOperationId();
						String uniqueOperationId = operation.getOperationId();
						int counter = 1;
						while (!usedOperationIds.add(uniqueOperationId)) {
							uniqueOperationId = initialOperationId + "_" + ++counter;
						}
						operation.setOperationId(uniqueOperationId);
					})
				);
	}

	@Override
	public void customise(OpenAPI openApi) {
		handleActuatorOperationIdUniqueness(openApi);
	}
}
