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

package org.springdoc.core.configuration;

import org.springdoc.core.customizers.SpecificationStringPropertiesCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.PropertyResolver;

/**
 * The type Spring doc specification string properties configuration.
 *
 * @author Anton Tkachenko tkachenkoas@gmail.com
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "springdoc.api-docs.specification-string-properties")
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocSpecificationStringPropertiesConfiguration {

    /**
     * Springdoc customizer that takes care of the specification string properties customization.
     *
     * @return the springdoc customizer
     */
    @Bean
    @ConditionalOnMissingBean
    @Lazy(false)
    SpecificationStringPropertiesCustomizer specificationStringPropertiesCustomizer(
            PropertyResolver propertyResolverUtils
    ) {
        return new SpecificationStringPropertiesCustomizer(propertyResolverUtils);
    }

}