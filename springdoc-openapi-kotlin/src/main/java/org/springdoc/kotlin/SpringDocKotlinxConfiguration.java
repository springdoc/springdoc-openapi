/*
 *
 *  *
 *  *  * Copyright 2019-2020 the original author or authors.
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package org.springdoc.kotlin;

import kotlinx.coroutines.flow.Flow;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;
import static org.springdoc.core.SpringDocUtils.getConfig;

/**
 * The type Spring doc kotlinx configuration.
 * @author bnasslahsen
 */
@ConditionalOnClass(Flow.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class SpringDocKotlinxConfiguration {

	/**
	 * Instantiates a new Spring doc kotlinx configuration.
	 */
	SpringDocKotlinxConfiguration() {
		getConfig().addFluxWrapperToIgnore(Flow.class);
	}

}