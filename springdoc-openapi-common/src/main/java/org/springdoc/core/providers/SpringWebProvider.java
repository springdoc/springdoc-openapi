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
package org.springdoc.core.providers;

import java.util.Map;
import java.util.Set;

import org.springdoc.core.SpringDocConfigProperties;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * The type Spring web provider.
 * @author bnasslahsen
 */
public abstract class SpringWebProvider implements ApplicationContextAware {

	/**
	 * The Application context.
	 */
	protected ApplicationContext applicationContext;

	/**
	 * The Handler methods.
	 */
	protected Map handlerMethods;

	/**
	 * Gets handler methods.
	 *
	 * @return the handler methods
	 */
	public abstract Map getHandlerMethods();

	/**
	 * Find path prefix string.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 * @return the string
	 */
	public abstract String findPathPrefix(SpringDocConfigProperties springDocConfigProperties);

	/**
	 * Gets active patterns.
	 *
	 * @param requestMappingInfo the request mapping info
	 * @return the active patterns
	 */
	public abstract Set<String> getActivePatterns(Object requestMappingInfo);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
