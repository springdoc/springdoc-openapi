/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
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

package org.springdoc.core;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.customizers.ActuatorOpenApiCustomizer;
import org.springdoc.core.customizers.ActuatorOperationCustomizer;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.Constants.ACTUATOR_DEFAULT_GROUP;
import static org.springdoc.core.Constants.ALL_PATTERN;
import static org.springdoc.core.Constants.DEFAULT_GROUP_NAME;
import static org.springdoc.core.Constants.HEALTH_PATTERN;
import static org.springdoc.core.Constants.MANAGEMENT_ENDPOINTS_WEB;

/**
 * The type Springdoc bean factory configurer.
 * @author bnasslahsen
 */
public class SpringdocActuatorBeanFactoryConfigurer extends SpringdocBeanFactoryConfigurer {

	/**
	 * The Grouped open apis.
	 */
	private final List<GroupedOpenApi> groupedOpenApis;

	/**
	 * Instantiates a new Springdoc actuator bean factory configurer.
	 *
	 * @param groupedOpenApis the grouped open apis
	 */
	public SpringdocActuatorBeanFactoryConfigurer(List<GroupedOpenApi> groupedOpenApis) {
		this.groupedOpenApis = groupedOpenApis;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		final BindResult<WebEndpointProperties> result = Binder.get(environment)
				.bind(MANAGEMENT_ENDPOINTS_WEB, WebEndpointProperties.class);
		if (result.isBound()) {
			WebEndpointProperties webEndpointProperties = result.get();

			List<GroupedOpenApi> newGroups = new ArrayList<>();

			ActuatorOpenApiCustomizer actuatorOpenApiCustomiser = new ActuatorOpenApiCustomizer(webEndpointProperties);
			beanFactory.registerSingleton("actuatorOpenApiCustomiser", actuatorOpenApiCustomiser);
			ActuatorOperationCustomizer actuatorCustomizer = new ActuatorOperationCustomizer();
			beanFactory.registerSingleton("actuatorCustomizer", actuatorCustomizer);

			GroupedOpenApi actuatorGroup = GroupedOpenApi.builder().group(ACTUATOR_DEFAULT_GROUP)
					.pathsToMatch(webEndpointProperties.getBasePath() + ALL_PATTERN)
					.pathsToExclude(webEndpointProperties.getBasePath() + HEALTH_PATTERN)
					.build();
			// Add the actuator group
			newGroups.add(actuatorGroup);

			if (CollectionUtils.isEmpty(groupedOpenApis)) {
				GroupedOpenApi defaultGroup = GroupedOpenApi.builder().group(DEFAULT_GROUP_NAME)
						.pathsToMatch(ALL_PATTERN)
						.pathsToExclude(webEndpointProperties.getBasePath() + ALL_PATTERN)
						.build();
				// Register the default group
				newGroups.add(defaultGroup);
			}

			newGroups.forEach(elt -> beanFactory.registerSingleton(elt.getGroup(), elt));
		}
		initBeanFactoryPostProcessor(beanFactory);
	}

}
