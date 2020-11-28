/*
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
 *
 */

package org.springdoc.core;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;

import static org.springdoc.core.Constants.ACTUATOR_DEFAULT_GROUP;
import static org.springdoc.core.Constants.DEFAULT_GROUP_NAME;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * The type Springdoc bean factory configurer.
 * @author bnasslahsen
 */
public class SpringdocActuatorBeanFactoryConfigurer implements EnvironmentAware, BeanFactoryPostProcessor {

	/**
	 * The Environment.
	 */
	@Nullable
	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	private List<GroupedOpenApi> groupedOpenApis;

	public SpringdocActuatorBeanFactoryConfigurer(List<GroupedOpenApi> groupedOpenApis) {
		this.groupedOpenApis = groupedOpenApis;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)  {
		final BindResult<WebEndpointProperties> result = Binder.get(environment)
				.bind("management.endpoints.web", WebEndpointProperties.class);
		if (result.isBound()) {
			WebEndpointProperties webEndpointProperties = result.get();

			List<GroupedOpenApi> newGroups = new ArrayList<>();
			GroupedOpenApi actuatorGroup = GroupedOpenApi.builder().group(ACTUATOR_DEFAULT_GROUP)
					.pathsToMatch(webEndpointProperties.getBasePath() + "/**")
					.pathsToExclude(webEndpointProperties.getBasePath() + "/health/*")
					.build();
			// Add the actuator group
			newGroups.add(actuatorGroup);

			if (groupedOpenApis.size() == 0) {
				GroupedOpenApi defaultGroup = GroupedOpenApi.builder().group(DEFAULT_GROUP_NAME)
						.pathsToMatch("/**")
						.pathsToExclude(webEndpointProperties.getBasePath() + "/**")
						.build();
				// Register the default group
				newGroups.add(defaultGroup);
			}

			newGroups.forEach(elt -> beanFactory.registerSingleton(elt.getGroup(), elt));
		}
		initBeanFactoryPostProcessor(beanFactory);
	}

	/**
	 * Init bean factory post processor.
	 *
	 * @param beanFactory the bean factory
	 */
	public static void initBeanFactoryPostProcessor(ConfigurableListableBeanFactory beanFactory) {
		for (String beanName : beanFactory.getBeanNamesForType(OpenAPIService.class))
			beanFactory.getBeanDefinition(beanName).setScope(SCOPE_PROTOTYPE);
		for (String beanName : beanFactory.getBeanNamesForType(OpenAPI.class))
			beanFactory.getBeanDefinition(beanName).setScope(SCOPE_PROTOTYPE);
	}
}
