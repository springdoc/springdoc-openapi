/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
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
package org.springdoc.webflux.core.providers;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.core.versions.HeaderVersionStrategy;
import org.springdoc.core.versions.MediaTypeVersionStrategy;
import org.springdoc.core.versions.PathVersionStrategy;
import org.springdoc.core.versions.QueryParamVersionStrategy;
import org.springdoc.core.versions.SpringDocApiVersionType;

import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.accept.ApiVersionResolver;
import org.springframework.web.reactive.accept.ApiVersionStrategy;
import org.springframework.web.reactive.accept.DefaultApiVersionStrategy;
import org.springframework.web.reactive.accept.HeaderApiVersionResolver;
import org.springframework.web.reactive.accept.MediaTypeParamApiVersionResolver;
import org.springframework.web.reactive.accept.PathApiVersionResolver;
import org.springframework.web.reactive.accept.QueryApiVersionResolver;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.AbstractHandlerMethodMapping;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;


/**
 * The type Spring webflux provider.
 *
 * @author bnasslahsen
 */
public class SpringWebFluxProvider extends SpringWebProvider {

	/**
	 * Instantiates a new Spring web flux provider.
	 *
	 * @param apiVersionStrategyOptional the api version strategy optional
	 */
	public SpringWebFluxProvider(Optional<ApiVersionStrategy> apiVersionStrategyOptional) {
		apiVersionStrategyOptional.ifPresent(apiVersionStrategy -> {
			try {
				DefaultApiVersionStrategy defaultApiVersionStrategy = (DefaultApiVersionStrategy) apiVersionStrategy;
				String defaultVersion = null;
				if(defaultApiVersionStrategy.getDefaultVersion() !=null)
					defaultVersion = defaultApiVersionStrategy.getDefaultVersion().toString();
				Field field = FieldUtils.getDeclaredField(DefaultApiVersionStrategy.class, "versionResolvers", true);
				final List<ApiVersionResolver> versionResolvers = (List<ApiVersionResolver>) field.get(defaultApiVersionStrategy);
				for (ApiVersionResolver apiVersionResolver : versionResolvers) {
					if (apiVersionResolver instanceof MediaTypeParamApiVersionResolver mediaTypeParamApiVersionResolver) {
						field = FieldUtils.getDeclaredField(MediaTypeParamApiVersionResolver.class, "compatibleMediaType", true);
						MediaType mediaType = (MediaType) field.get(mediaTypeParamApiVersionResolver);
						field = FieldUtils.getDeclaredField(MediaTypeParamApiVersionResolver.class, "parameterName", true);
						String parameterName = (String) field.get(mediaTypeParamApiVersionResolver);
						MediaTypeVersionStrategy mediaTypeStrategy = new MediaTypeVersionStrategy(mediaType, parameterName, defaultVersion);
						springDocVersionStrategyMap.put(SpringDocApiVersionType.MEDIA_TYPE, mediaTypeStrategy);
					}
					else if (apiVersionResolver instanceof PathApiVersionResolver pathApiVersionResolver) {
						field = FieldUtils.getDeclaredField(PathApiVersionResolver.class, "pathSegmentIndex", true);
						Integer pathSegmentIndex = (Integer) field.get(pathApiVersionResolver);
						PathVersionStrategy pathVersionStrategy = new PathVersionStrategy(pathSegmentIndex, defaultVersion);
						springDocVersionStrategyMap.put(SpringDocApiVersionType.PATH, pathVersionStrategy);
					}
					else if (apiVersionResolver instanceof HeaderApiVersionResolver headerApiVersionResolver) {
						field = FieldUtils.getDeclaredField(HeaderApiVersionResolver.class, "headerName", true);
						String headerName = (String) field.get(headerApiVersionResolver);
						HeaderVersionStrategy headerVersionStrategy = new HeaderVersionStrategy(headerName, defaultVersion);
						springDocVersionStrategyMap.put(SpringDocApiVersionType.HEADER, headerVersionStrategy);
					}
					else if (apiVersionResolver instanceof QueryApiVersionResolver queryApiVersionResolver) {
						field = FieldUtils.getDeclaredField(QueryApiVersionResolver.class, "queryParamName", true);
						String queryParamName = (String) field.get(queryApiVersionResolver);
						QueryParamVersionStrategy queryParamVersionStrategy = new QueryParamVersionStrategy(queryParamName, defaultVersion);
						springDocVersionStrategyMap.put(SpringDocApiVersionType.QUERY_PARAM, queryParamVersionStrategy);
					}
				}
			}
			catch (IllegalAccessException e) {
				LOGGER.warn(e.getMessage());
			}
		});
	}

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
		RequestMappingInfo requestMappingInfo = (RequestMappingInfo) requestMapping;
		PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
		return patternsRequestCondition.getPatterns().stream()
				.map(PathPattern::getPatternString).collect(Collectors.toCollection(LinkedHashSet::new));
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
					.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a1, a2) -> a1, LinkedHashMap::new));
		}
		return this.handlerMethods;
	}
}
