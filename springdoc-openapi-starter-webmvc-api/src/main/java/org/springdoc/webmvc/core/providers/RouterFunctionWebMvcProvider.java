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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springdoc.core.fn.AbstractRouterFunctionVisitor;
import org.springdoc.core.providers.RouterFunctionProvider;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;

/**
 * The type Router function provider.
 *
 * @author bnasslahsen
 */
//To keep compatiblity with spring-boot 1
public class RouterFunctionWebMvcProvider implements RouterFunctionProvider, ApplicationContextAware {

	/**
	 * The Application context.
	 */
	private ApplicationContext applicationContext;

	/**
	 * Gets web mvc router function paths.
	 *
	 * @return the web mvc router function paths
	 */
	public Optional<Map<String, AbstractRouterFunctionVisitor>> getRouterFunctionPaths() {
		Map<String, RouterFunction> routerBeans = applicationContext.getBeansOfType(RouterFunction.class);
		if (CollectionUtils.isEmpty(routerBeans))
			return Optional.empty();
		Map<String, AbstractRouterFunctionVisitor> routerFunctionVisitorMap = new HashMap<>();
		for (Map.Entry<String, RouterFunction> entry : routerBeans.entrySet()) {
			RouterFunction routerFunction = entry.getValue();
			RouterFunctionVisitor routerFunctionVisitor = new RouterFunctionVisitor();
			routerFunction.accept(routerFunctionVisitor);
			routerFunctionVisitorMap.put(entry.getKey(), routerFunctionVisitor);
		}
		return Optional.of(routerFunctionVisitorMap);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * The type Router function visitor.
	 *
	 * @author bnasslahsen
	 */
	private class RouterFunctionVisitor extends AbstractRouterFunctionVisitor implements RouterFunctions.Visitor, RequestPredicates.Visitor {
		@Override
		public void route(RequestPredicate predicate, HandlerFunction<?> handlerFunction) {
			this.currentRouterFunctionDatas = new ArrayList<>();
			predicate.accept(this);
			commonRoute();
		}

		@Override
		public void resources(Function<ServerRequest, Optional<Resource>> lookupFunction) {
			// Not yet needed
		}

		@Override
		public void unknown(RouterFunction<?> routerFunction) {
			// Not yet needed
		}

		@Override
		public void unknown(RequestPredicate predicate) {
			// Not yet needed
		}

		@Override
		public void startNested(RequestPredicate predicate) {
			commonStartNested();
			predicate.accept(this);
		}


		@Override
		public void endNested(RequestPredicate predicate) {
			commonEndNested();
		}

	}
}
