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

package org.springdoc.core;

import java.util.Arrays;

import org.springdoc.core.customizers.SpecPropertiesCustomizer;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.PropertyResolver;

/**
 * The type Spring doc specification string properties configuration.
 *
 * @author Anton Tkachenko tkachenkoas@gmail.com
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@Conditional(SpecPropertiesCondition.class)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocSpecPropertiesConfiguration {

    /**
     * Springdoc customizer that takes care of the specification string properties customization.
     * Will be applied to general openapi schema.
     *
     * @return the springdoc customizer
     */
    @Bean
    @ConditionalOnMissingBean
    @Lazy(false)
	SpecPropertiesCustomizer specificationStringPropertiesCustomizer(
            PropertyResolver propertyResolverUtils
    ) {
        return new SpecPropertiesCustomizer(propertyResolverUtils);
    }

    /**
     * Bean post processor that applies the specification string properties customization to
     * grouped openapi schemas by using group name as a prefix for properties.
     *
     * @return the bean post processor
     */
    @Bean
    @ConditionalOnMissingBean
    @Lazy(false)
    SpecificationStringPropertiesCustomizerBeanPostProcessor specificationStringPropertiesCustomizerBeanPostProcessor(
            PropertyResolver propertyResolverUtils
    ) {
        return new SpecificationStringPropertiesCustomizerBeanPostProcessor(propertyResolverUtils);
    }


	/**
	 * The type Specification string properties customizer bean post processor.
	 */
	private static class SpecificationStringPropertiesCustomizerBeanPostProcessor implements BeanPostProcessor {

        private final PropertyResolver propertyResolverUtils;

        public SpecificationStringPropertiesCustomizerBeanPostProcessor(
                PropertyResolver propertyResolverUtils
        ) {
            this.propertyResolverUtils = propertyResolverUtils;
        }

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) {
			if (bean instanceof GroupedOpenApi) {
				GroupedOpenApi groupedOpenApi = (GroupedOpenApi) bean;
				groupedOpenApi.addAllOpenApiCustomizer(
						Arrays.asList(
								new SpecPropertiesCustomizer(
										propertyResolverUtils,
										groupedOpenApi.getGroup()
								)
						)
				);
			}
			return bean;
		}
    }
	

}