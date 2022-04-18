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
 *
 */

package org.springdoc.core;

import org.springdoc.core.customizers.ActuatorOpenApiCustomizer;
import org.springdoc.core.customizers.ActuatorOperationCustomizer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ObjectUtils;

import static org.springdoc.core.Constants.ACTUATOR_DEFAULT_GROUP;
import static org.springdoc.core.Constants.ALL_PATTERN;
import static org.springdoc.core.Constants.DEFAULT_GROUP_NAME;
import static org.springdoc.core.Constants.HEALTH_PATTERN;
import static org.springdoc.core.Constants.MANAGEMENT_ENDPOINTS_WEB;

/**
 * The type Springdoc bean factory configurer.
 * @author bnasslahsen
 * @author christophejan
 */
public class SpringdocActuatorBeanFactoryConfigurer implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

	/**
	 * The ApplicationContext.
	 */
	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		final BindResult<WebEndpointProperties> result = Binder.get(applicationContext.getEnvironment())
				.bind(MANAGEMENT_ENDPOINTS_WEB, WebEndpointProperties.class);
		if (result.isBound()) {
			WebEndpointProperties webEndpointProperties = result.get();

			boolean addDefaultGroup = ObjectUtils.isEmpty(applicationContext
					.getBeanNamesForType(GroupedOpenApi.class, true, false));

			registry.registerBeanDefinition("actuatorOpenApiCustomiser", BeanDefinitionBuilder
					.genericBeanDefinition(ActuatorOpenApiCustomizer.class)
					.addConstructorArgValue(webEndpointProperties)
					.getBeanDefinition());

			registry.registerBeanDefinition("actuatorCustomizer", BeanDefinitionBuilder
					.genericBeanDefinition(ActuatorOperationCustomizer.class)
					.getBeanDefinition());

			// register the actuator group bean definition
			registry.registerBeanDefinition(ACTUATOR_DEFAULT_GROUP, BeanDefinitionBuilder
					.genericBeanDefinition(SpringdocActuatorBeanFactoryConfigurer.class)
					.setFactoryMethod("actuatorGroupFactoryMethod")
					.addConstructorArgValue(webEndpointProperties.getBasePath())
					.addConstructorArgValue(new RuntimeBeanReference(ActuatorOpenApiCustomizer.class))
					.addConstructorArgValue(new RuntimeBeanReference(ActuatorOperationCustomizer.class))
					.getBeanDefinition());

			if (addDefaultGroup) {
				// register the default group bean definition
				registry.registerBeanDefinition(DEFAULT_GROUP_NAME, BeanDefinitionBuilder
						.genericBeanDefinition(SpringdocActuatorBeanFactoryConfigurer.class)
						.setFactoryMethod("defaultGroupFactoryMethod")
						.addConstructorArgValue(webEndpointProperties.getBasePath())
						.getBeanDefinition());
			}
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		SpringdocBeanFactoryConfigurer.initBeanFactoryPostProcessor(beanFactory);
	}

	/**
	 * Actuator {@link GroupedOpenApi} factory method.
	 *
	 * @param actuatorBasePath the actuator base path
	 * @param actuatorOpenApiCustomiser the {@link ActuatorOpenApiCustomizer}
	 * @param actuatorOperationCustomizer the {@link ActuatorOperationCustomizer}
	 * 
	 * @return the actuator {@link GroupedOpenApi}
	 */
	public static GroupedOpenApi actuatorGroupFactoryMethod(String actuatorBasePath,
			ActuatorOpenApiCustomizer actuatorOpenApiCustomiser, ActuatorOperationCustomizer actuatorOperationCustomizer) {
		return GroupedOpenApi.builder().group(ACTUATOR_DEFAULT_GROUP)
				.pathsToMatch(actuatorBasePath + ALL_PATTERN)
				.pathsToExclude(actuatorBasePath + HEALTH_PATTERN)
				.addOpenApiCustomiser(actuatorOpenApiCustomiser)
				.addOperationCustomizer(actuatorOperationCustomizer)
				.build();
	}

	/**
	 * Default {@link GroupedOpenApi} factory method.
	 *
	 * @param actuatorBasePath the actuator base path
	 * 
	 * @return the default {@link GroupedOpenApi}
	 */
	public static GroupedOpenApi defaultGroupFactoryMethod(String actuatorBasePath) {
		return GroupedOpenApi.builder().group(DEFAULT_GROUP_NAME)
				.pathsToMatch(ALL_PATTERN)
				.pathsToExclude(actuatorBasePath + ALL_PATTERN)
				.build();
	}

}
