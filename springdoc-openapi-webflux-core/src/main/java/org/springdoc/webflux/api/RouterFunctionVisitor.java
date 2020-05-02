package org.springdoc.webflux.api;

import java.util.Set;
import java.util.function.Function;

import org.springdoc.core.models.RouterFunctionData;
import reactor.core.publisher.Mono;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;

public class RouterFunctionVisitor implements RouterFunctions.Visitor, RequestPredicates.Visitor {

	private Set<HttpMethod> methods;

	private String path;

	private String consumes;

	private String header;

	private String queryParam;

	private RequestPredicate routePredicate;

	private HandlerFunction<?> routeHandlerFunction;

	private RequestPredicate unknownPredicate;

	private Function<ServerRequest, Mono<Resource>> lookupFunction;

	private RouterFunction unknownRouterFunction;

	@Override
	public void startNested(RequestPredicate predicate) {
	}

	@Override
	public void endNested(RequestPredicate predicate) {
	}

	@Override
	public void route(RequestPredicate predicate, HandlerFunction<?> handlerFunction) {
		this.routePredicate = predicate;
		this.routeHandlerFunction = handlerFunction;
		predicate.accept(this);
	}

	@Override
	public void resources(Function<ServerRequest, Mono<Resource>> lookupFunction) {
		this.lookupFunction = lookupFunction;
	}

	@Override
	public void unknown(RouterFunction<?> routerFunction) {
		this.unknownRouterFunction = routerFunction;
	}

	// RequestPredicates.Visitor

	@Override
	public void method(Set<HttpMethod> methods) {
		this.methods = methods;
	}

	@Override
	public void path(String pattern) {
		this.path = pattern;
	}

	@Override
	public void pathExtension(String extension) {
	}

	@Override
	public void header(String name, String value) {
		if (HttpHeaders.ACCEPT.equals(name))
			this.consumes = value;
		else
			this.header = name + "=" + value;
	}

	@Override
	public void queryParam(String name, String value) {
		this.queryParam = name + "=" + value;
	}

	@Override
	public void startAnd() {
	}

	@Override
	public void and() {
		//TODO
	}

	@Override
	public void endAnd() {
	}

	@Override
	public void startOr() {
	}

	@Override
	public void or() {
	}

	@Override
	public void endOr() {
	}

	@Override
	public void startNegate() {
	}

	@Override
	public void endNegate() {
	}

	@Override
	public void unknown(RequestPredicate predicate) {
		this.unknownPredicate = predicate;
	}

	public RouterFunctionData getRouterFunctionData() {
		return new RouterFunctionData(this.path, this.consumes, this.header, this.queryParam, this.methods);
	}
}


