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

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static org.springdoc.core.Constants.SPRINGDOC_SPEC_PROPERTIES_PREFIX;

/**
 * The type Spec properties condition.
 *
 * @author bnasslahsen
 */
public class SpecPropertiesCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		MutablePropertySources propertySources = ((ConfigurableEnvironment) context.getEnvironment())
				.getPropertySources();
		for (PropertySource<?> propertySource : propertySources) {
			if (propertySource instanceof EnumerablePropertySource<?>) {
				String[] propertyNames = ((EnumerablePropertySource<?>) propertySource).getPropertyNames();
				for (String name : propertyNames) {
					if (name.startsWith(SPRINGDOC_SPEC_PROPERTIES_PREFIX)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
