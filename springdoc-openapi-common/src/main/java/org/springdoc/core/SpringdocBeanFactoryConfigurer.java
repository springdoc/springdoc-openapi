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

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import static org.springdoc.core.Constants.SPRINGDOC_PREFIX;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * The type Springdoc bean factory configurer.
 * @author bnasslahsen
 */
public class SpringdocBeanFactoryConfigurer implements EnvironmentAware, BeanFactoryPostProcessor {

	/**
	 * The Environment.
	 */
	@Nullable
	protected Environment environment;

	/**
	 * Init bean factory post processor.
	 *
	 * @param beanFactory the bean factory
	 */
	public static void initBeanFactoryPostProcessor(ConfigurableListableBeanFactory beanFactory) {
		for (String beanName : beanFactory.getBeanNamesForType(OpenAPIService.class, true, false))
			beanFactory.getBeanDefinition(beanName).setScope(SCOPE_PROTOTYPE);
		for (String beanName : beanFactory.getBeanNamesForType(OpenAPI.class, true, false))
			beanFactory.getBeanDefinition(beanName).setScope(SCOPE_PROTOTYPE);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		final BindResult<SpringDocConfigProperties> result = Binder.get(environment)
				.bind(SPRINGDOC_PREFIX, SpringDocConfigProperties.class);
		if (result.isBound()) {
			SpringDocConfigProperties springDocGroupConfig = result.get();
			List<GroupedOpenApi> groupedOpenApis = springDocGroupConfig.getGroupConfigs().stream()
					.map(elt -> {
						GroupedOpenApi.Builder builder = GroupedOpenApi.builder();
						if (!CollectionUtils.isEmpty(elt.getPackagesToScan()))
							builder.packagesToScan(elt.getPackagesToScan().toArray(new String[0]));
						if (!CollectionUtils.isEmpty(elt.getPathsToMatch()))
							builder.pathsToMatch(elt.getPathsToMatch().toArray(new String[0]));
						if (!CollectionUtils.isEmpty(elt.getProducesToMatch()))
							builder.producesToMatch(elt.getProducesToMatch().toArray(new String[0]));
						if (!CollectionUtils.isEmpty(elt.getConsumesToMatch()))
							builder.consumesToMatch(elt.getConsumesToMatch().toArray(new String[0]));
						if (!CollectionUtils.isEmpty(elt.getHeadersToMatch()))
							builder.headersToMatch(elt.getHeadersToMatch().toArray(new String[0]));
						if (!CollectionUtils.isEmpty(elt.getPathsToExclude()))
							builder.pathsToExclude(elt.getPathsToExclude().toArray(new String[0]));
						if (!CollectionUtils.isEmpty(elt.getPackagesToExclude()))
							builder.packagesToExclude(elt.getPackagesToExclude().toArray(new String[0]));
						if (StringUtils.isNotEmpty(elt.getDisplayName()))
							builder.displayName(elt.getDisplayName());
						return builder.group(elt.getGroup()).build();
					})
					.collect(Collectors.toList());
			groupedOpenApis.forEach(elt -> beanFactory.registerSingleton(elt.getGroup(), elt));
		}
		initBeanFactoryPostProcessor(beanFactory);
	}

}
