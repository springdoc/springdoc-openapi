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

import org.springdoc.core.models.GroupedOpenApi;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

import static org.springdoc.core.utils.Constants.SPRINGDOC_CACHE_DISABLED;

/**
 * The type Cache or grouped open api condition.
 *
 * @author bnasslahsen
 */
public class CacheOrGroupedOpenApiCondition extends AnyNestedCondition {

	/**
	 * Instantiates a new Cache or grouped open api condition.
	 */
	CacheOrGroupedOpenApiCondition() {
		super(ConfigurationPhase.REGISTER_BEAN);
	}

	/**
	 * The type On multiple open api support condition.
	 *
	 * @author bnasslahsen
	 */
	@Conditional(MultipleOpenApiSupportCondition.class)
	static class OnMultipleOpenApiSupportCondition {}

	/**
	 * The type On cache disabled.
	 *
	 * @author bnasslahsen
	 */
	@ConditionalOnProperty(name = SPRINGDOC_CACHE_DISABLED)
	@ConditionalOnMissingBean(GroupedOpenApi.class)
	static class OnCacheDisabled {}

}