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


import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import static org.springdoc.core.Constants.GROUP_CONFIG_FIRST_PROPERTY;

/**
 * The type Multiple open api support condition.
 * @author bnasslahsen
 */
public class MultipleOpenApiGroupsCondition extends AnyNestedCondition {

	/**
	 * Instantiates a new Multiple open api support condition.
	 */
	MultipleOpenApiGroupsCondition() {
		super(ConfigurationPhase.REGISTER_BEAN);
	}

	/**
	 * The type On grouped open api bean.
	 * @author bnasslahsen
	 */
	@ConditionalOnBean(GroupedOpenApi.class)
	static class OnGroupedOpenApiBean {}

	/**
	 * The type On group config property.
	 * @author bnasslahsen
	 */
	@ConditionalOnProperty(name = GROUP_CONFIG_FIRST_PROPERTY)
	static class OnGroupConfigProperty {}

}