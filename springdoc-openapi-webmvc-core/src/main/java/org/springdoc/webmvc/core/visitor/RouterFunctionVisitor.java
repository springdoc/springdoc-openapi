package org.springdoc.webmvc.core.visitor;

import java.util.Optional;
import java.util.function.Function;

import org.springdoc.core.models.RouterFunctionData;
import org.springdoc.core.visitor.AbstractRouterFunctionVisitor;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;

public class RouterFunctionVisitor extends AbstractRouterFunctionVisitor implements RouterFunctions.Visitor, RequestPredicates.Visitor {

	@Override
	public void route(RequestPredicate predicate, HandlerFunction<?> handlerFunction) {
		this.routerFunctionData = new RouterFunctionData();
		routerFunctionDatas.add(this.routerFunctionData);
		predicate.accept(this);
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
		// Not yet needed
	}

	@Override
	public void endNested(RequestPredicate predicate) {
		// Not yet needed
	}

}


