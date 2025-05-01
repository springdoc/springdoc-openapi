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

package org.springdoc.core.configuration;

import java.util.List;
import java.util.Set;

import org.springdoc.core.conditions.SpecPropertiesCondition;
import org.springdoc.core.customizers.SpecPropertiesCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties.GroupConfig;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * The type Spring doc specification string properties configuration.
 *
 * @author Anton Tkachenko tkachenkoas@gmail.com
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(SpringDocConfiguration.class)
@Conditional(SpecPropertiesCondition.class)
public class SpringDocSpecPropertiesConfiguration {

	/**
	 * Springdoc customizer that takes care of the specification string properties customization.
	 * Will be applied to general openapi schema.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 * @return the springdoc customizer
	 */
	@Bean
    @ConditionalOnMissingBean
    @Lazy(false)
	SpecPropertiesCustomizer specificationStringPropertiesCustomizer(
            SpringDocConfigProperties springDocConfigProperties
    ) {
        return new SpecPropertiesCustomizer(springDocConfigProperties);
    }

	/**
	 * Bean post processor that applies the specification string properties customization to
	 * grouped openapi schemas by using group name as a prefix for properties.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 * @return the bean post processor
	 */
	@Bean
    @ConditionalOnMissingBean
    @Lazy(false)
    SpecificationStringPropertiesCustomizerBeanPostProcessor specificationStringPropertiesCustomizerBeanPostProcessor(
			SpringDocConfigProperties springDocConfigProperties
    ) {
        return new SpecificationStringPropertiesCustomizerBeanPostProcessor(springDocConfigProperties);
    }


	/**
	 * The type Specification string properties customizer bean post processor.
	 */
	public static class SpecificationStringPropertiesCustomizerBeanPostProcessor implements BeanPostProcessor {

		/**
		 * The Spring doc config properties.
		 */
		private final SpringDocConfigProperties springDocConfigProperties;

		/**
		 * Instantiates a new Specification string properties customizer bean post processor.
		 *
		 * @param springDocConfigProperties the spring doc config properties
		 */
		public SpecificationStringPropertiesCustomizerBeanPostProcessor(
				SpringDocConfigProperties springDocConfigProperties
        ) {
            this.springDocConfigProperties = springDocConfigProperties;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) {
            if (bean instanceof GroupedOpenApi groupedOpenApi) {
               Set<GroupConfig> groupConfigs = springDocConfigProperties.getGroupConfigs();
				for (GroupConfig groupConfig : groupConfigs) {
					if(groupConfig.getGroup().equals(groupedOpenApi.getGroup())) {
						groupedOpenApi.addAllOpenApiCustomizer(List.of(new SpecPropertiesCustomizer(
								groupConfig.getOpenApi()
						)));
					}
				}
            }
            return bean;
        }
    }
	

}