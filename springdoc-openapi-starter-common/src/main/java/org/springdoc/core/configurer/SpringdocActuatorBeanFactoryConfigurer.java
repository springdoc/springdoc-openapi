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

package org.springdoc.core.configurer;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.customizers.ActuatorOperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.utils.Constants.ACTUATOR_DEFAULT_GROUP;
import static org.springdoc.core.utils.Constants.ALL_PATTERN;
import static org.springdoc.core.utils.Constants.DEFAULT_GROUP_NAME;
import static org.springdoc.core.utils.Constants.HEALTH_PATTERN;
import static org.springdoc.core.utils.Constants.MANAGEMENT_ENDPOINTS_WEB;
import static org.springdoc.core.utils.Constants.SPRINGDOC_PREFIX;

/**
 * The type Springdoc bean factory configurer.
 *
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
		final BindResult<SpringDocConfigProperties> springDocConfigPropertiesBindResult = Binder.get(environment)
				.bind(SPRINGDOC_PREFIX, SpringDocConfigProperties.class);
		
		if (result.isBound() && springDocConfigPropertiesBindResult.isBound()) {
			WebEndpointProperties webEndpointProperties = result.get();
			SpringDocConfigProperties springDocConfigProperties = springDocConfigPropertiesBindResult.get();
			List<GroupedOpenApi> newGroups = new ArrayList<>();
			
			ActuatorOperationCustomizer actuatorCustomizer = new ActuatorOperationCustomizer(springDocConfigProperties);
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
