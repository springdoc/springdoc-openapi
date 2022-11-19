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

import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

import static org.springdoc.core.Constants.SPRINGDOC_SHOW_ACTUATOR;

/**
 * The type Multiple open api support condition.
 * @author bnasslashen
 */
public class MultipleOpenApiSupportCondition extends AnyNestedCondition {

	/**
	 * Instantiates a new Multiple open api support condition.
	 */
	MultipleOpenApiSupportCondition() {
		super(ConfigurationPhase.REGISTER_BEAN);
	}

	/**
	 * The type On multiple open api support condition.
	 * @author bnasslahsen
	 */
	@Conditional(MultipleOpenApiGroupsCondition.class)
	static class OnMultipleOpenApiSupportCondition {}

	/**
	 * The type On actuator different port.
	 * @author bnasslashen
	 */
	@ConditionalOnManagementPort(ManagementPortType.DIFFERENT)
	@ConditionalOnProperty(SPRINGDOC_SHOW_ACTUATOR)
	static class OnActuatorDifferentPort {}

}