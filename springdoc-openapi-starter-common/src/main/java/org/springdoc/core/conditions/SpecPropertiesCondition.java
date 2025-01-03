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

package org.springdoc.core.conditions;

/**
 * @author bnasslahsen
 */

import java.util.Set;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties.GroupConfig;

import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static org.springdoc.core.utils.Constants.SPRINGDOC_PREFIX;

/**
 * The type Spec properties condition.
 *
 * @author bnasslahsen
 */
public class SpecPropertiesCondition implements Condition {
	
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		final BindResult<SpringDocConfigProperties> result = Binder.get(context.getEnvironment())
				.bind(SPRINGDOC_PREFIX, SpringDocConfigProperties.class);
		if (result.isBound()) {
			SpringDocConfigProperties springDocConfigProperties = result.get();
			if (springDocConfigProperties.getOpenApi() != null)
				return true;
			Set<GroupConfig> groupConfigs = springDocConfigProperties.getGroupConfigs();
			return groupConfigs.stream().anyMatch(config -> config.getOpenApi() != null);
		}
		return false;
	}
	
}
