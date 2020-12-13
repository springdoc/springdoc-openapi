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

package org.springdoc.webflux.core.visitor;

import java.util.ArrayList;
import java.util.function.Function;

import org.springdoc.core.fn.AbstractRouterFunctionVisitor;
import reactor.core.publisher.Mono;

import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;

/**
 * The type Router function visitor.
 * @author bnasslahsen
 */
public class RouterFunctionVisitor extends AbstractRouterFunctionVisitor implements RouterFunctions.Visitor, RequestPredicates.Visitor {

	@Override
	public void route(RequestPredicate predicate, HandlerFunction<?> handlerFunction) {
		this.currentRouterFunctionDatas = new ArrayList<>();
		predicate.accept(this);
		commonRoute();
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

	@Override
	public void resources(Function<ServerRequest, Mono<Resource>> lookupFunction) {
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
}


