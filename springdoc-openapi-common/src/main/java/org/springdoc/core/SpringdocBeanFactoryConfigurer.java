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

import io.swagger.v3.oas.models.OpenAPI;

import org.springdoc.core.SpringDocConfigProperties.GroupConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.Constants.SPRINGDOC_PREFIX;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * The type Springdoc bean factory configurer.
 * @author bnasslahsen
 * @author christophejan
 */
public class SpringdocBeanFactoryConfigurer implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

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
		final BindResult<SpringDocConfigProperties> result = Binder.get(applicationContext.getEnvironment())
				.bind(SPRINGDOC_PREFIX, SpringDocConfigProperties.class);
		if (result.isBound()) {
			result.get().getGroupConfigs().stream().forEach(groupConfig ->
				// register bean definitions
				registry.registerBeanDefinition(groupConfig.getGroup(), BeanDefinitionBuilder
						.genericBeanDefinition(SpringdocBeanFactoryConfigurer.class)
						.setFactoryMethod("groupedOpenApisFactoryMethod")
						.addConstructorArgValue(groupConfig)
						.getBeanDefinition())
			);
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		initBeanFactoryPostProcessor(beanFactory);
	}

	/**
	 * {@link GroupedOpenApi} factory method from {@link GroupConfig}.
	 *
	 * @param groupConfig the {@link GroupConfig}
	 * 
	 * @return the {@link GroupedOpenApi}
	 */
	public static GroupedOpenApi groupedOpenApisFactoryMethod(GroupConfig groupConfig) {
		GroupedOpenApi.Builder builder = GroupedOpenApi.builder();
		if (!CollectionUtils.isEmpty(groupConfig.getPackagesToScan()))
			builder.packagesToScan(groupConfig.getPackagesToScan().toArray(new String[0]));
		if (!CollectionUtils.isEmpty(groupConfig.getPathsToMatch()))
			builder.pathsToMatch(groupConfig.getPathsToMatch().toArray(new String[0]));
		return builder.group(groupConfig.getGroup()).build();
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
