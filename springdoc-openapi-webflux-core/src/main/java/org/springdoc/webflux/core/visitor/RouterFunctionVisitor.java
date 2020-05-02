package org.springdoc.webflux.core.visitor;

import java.util.function.Function;

import org.springdoc.core.models.RouterFunctionData;
import org.springdoc.core.visitor.AbstractRouterFunctionVisitor;
import reactor.core.publisher.Mono;

import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;

public class RouterFunctionVisitor extends AbstractRouterFunctionVisitor implements RouterFunctions.Visitor, RequestPredicates.Visitor {

	@Override
	public void route(RequestPredicate predicate, HandlerFunction<?> handlerFunction) {
		this.routerFunctionData = new RouterFunctionData();
		routerFunctionDatas.add(this.routerFunctionData);
		predicate.accept(this);
	}

	@Override
	public void startNested(RequestPredicate predicate) {
		// Not yet needed
	}

	@Override
	public void endNested(RequestPredicate predicate) {
		// Not yet needed
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


