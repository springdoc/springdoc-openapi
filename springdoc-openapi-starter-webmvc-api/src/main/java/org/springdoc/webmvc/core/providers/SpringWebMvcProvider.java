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
package org.springdoc.webmvc.core.providers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringWebProvider;

import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * The type Spring web mvc provider.
 *
 * @author bnasslahsen
 */
public class SpringWebMvcProvider extends SpringWebProvider {

	/**
	 * Finds path prefix.
	 *
	 * @param springDocConfigProperties the spring doc config properties
	 * @return the path prefix
	 */
	@Override
	public String findPathPrefix(SpringDocConfigProperties springDocConfigProperties) {
		Map<RequestMappingInfo, HandlerMethod> map = getHandlerMethods();
		for (Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
			RequestMappingInfo requestMappingInfo = entry.getKey();
			Set<String> patterns = getActivePatterns(requestMappingInfo);
			if (!CollectionUtils.isEmpty(patterns)) {
				for (String operationPath : patterns) {
					if (operationPath.endsWith(springDocConfigProperties.getApiDocs().getPath()))
						return operationPath.replace(springDocConfigProperties.getApiDocs().getPath(), StringUtils.EMPTY);
				}
			}
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Gets active patterns.
	 *
	 * @param requestMapping the request mapping info
	 * @return the active patterns
	 */
	public Set<String> getActivePatterns(Object requestMapping) {
		Set<String> patterns = null;
		RequestMappingInfo requestMappingInfo = (RequestMappingInfo) requestMapping;
		PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
		if (patternsRequestCondition != null)
			patterns = patternsRequestCondition.getPatterns();
		else {
			PathPatternsRequestCondition pathPatternsRequestCondition = requestMappingInfo.getPathPatternsCondition();
			if (pathPatternsRequestCondition != null)
				patterns = pathPatternsRequestCondition.getPatternValues();
		}
		return patterns;
	}


	/**
	 * Gets handler methods.
	 *
	 * @return the handler methods
	 */
	@Override
	public Map getHandlerMethods() {
		if (this.handlerMethods == null) {
			Map<String, RequestMappingHandlerMapping> beansOfTypeRequestMappingHandlerMapping = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);
			this.handlerMethods = beansOfTypeRequestMappingHandlerMapping.values().stream()
					.map(AbstractHandlerMethodMapping::getHandlerMethods)
					.map(Map::entrySet)
					.flatMap(Collection::stream)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1, LinkedHashMap::new));
		}
		return this.handlerMethods;
	}
}
