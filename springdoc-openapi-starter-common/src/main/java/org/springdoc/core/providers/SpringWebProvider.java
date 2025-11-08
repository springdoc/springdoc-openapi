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
package org.springdoc.core.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.versions.SpringDocApiVersionType;
import org.springdoc.core.versions.SpringDocVersionStrategy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

/**
 * The type Spring web provider.
 *
 * @author bnasslahsen
 */
public abstract class SpringWebProvider implements ApplicationContextAware {

	/**
	 * The constant LOGGER.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(SpringWebProvider.class);

	/**
	 * The Application context.
	 */
	protected ApplicationContext applicationContext;

	/**
	 * The Handler methods.
	 */
	protected Map handlerMethods;

	/**
	 * The Spring doc version strategy map.
	 */
	protected final Map<SpringDocApiVersionType, SpringDocVersionStrategy> springDocVersionStrategyMap = new HashMap<>();

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

	/**
	 * Gets spring doc version strategy.
	 *
	 * @param version the version
	 * @param params  the params
	 * @return the spring doc version strategy
	 */
	public SpringDocVersionStrategy getSpringDocVersionStrategy(String version, String[] params) {
		SpringDocVersionStrategy springDocVersionStrategy = null;
		if (!CollectionUtils.isEmpty(springDocVersionStrategyMap)) {
			if (springDocVersionStrategyMap.size() == 1)
				springDocVersionStrategy = springDocVersionStrategyMap.values().iterator().next();
			else
				springDocVersionStrategy = resolveApiVersionStrategy(version, params);
			springDocVersionStrategy.updateVersion(version, params);
		}
		return springDocVersionStrategy;
	}

	/**
	 * Resolve api version strategy spring doc version strategy.
	 *
	 * @param version the version
	 * @param params  the params
	 * @return the spring doc version strategy
	 */
	private SpringDocVersionStrategy resolveApiVersionStrategy(String version, String[] params) {
		if (version != null) {
			if (springDocVersionStrategyMap.containsKey(SpringDocApiVersionType.PATH))
				return springDocVersionStrategyMap.get(SpringDocApiVersionType.PATH);
			else if (springDocVersionStrategyMap.containsKey(SpringDocApiVersionType.HEADER))
				return springDocVersionStrategyMap.get(SpringDocApiVersionType.HEADER);
			else if (springDocVersionStrategyMap.containsKey(SpringDocApiVersionType.MEDIA_TYPE))
				return springDocVersionStrategyMap.get(SpringDocApiVersionType.MEDIA_TYPE);
		}
		if (ArrayUtils.isNotEmpty(params) && springDocVersionStrategyMap.containsKey(SpringDocApiVersionType.QUERY_PARAM))
			return springDocVersionStrategyMap.get(SpringDocApiVersionType.QUERY_PARAM);
		return springDocVersionStrategyMap.values().iterator().next();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
