package org.springdoc.webflux.api;

import java.util.ArrayList;
import java.util.List;
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

	private List<RouterFunctionData> routerFunctionDatas = new ArrayList<>();

	private RouterFunctionData routerFunctionData;

	@Override
	public void route(RequestPredicate predicate, HandlerFunction<?> handlerFunction) {
		this.routerFunctionData = new RouterFunctionData();
		routerFunctionDatas.add(this.routerFunctionData);
		predicate.accept(this);
	}

	@Override
	public void method(Set<HttpMethod> methods) {
		routerFunctionData.setMethods(methods);
	}

	@Override
	public void path(String pattern) {
		routerFunctionData.setPath(pattern);
	}

	@Override
	public void header(String name, String value) {
		if (HttpHeaders.ACCEPT.equals(name))
			routerFunctionData.setConsumes(value);
		else
			routerFunctionData.setHeaders(name + "=" + value);
	}

	public List<RouterFunctionData> getRouterFunctionDatas() {
		return routerFunctionDatas;
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
	public void pathExtension(String extension) {
		// Not yet needed
	}


	@Override
	public void queryParam(String name, String value) {
		// Not yet needed
	}

	@Override
	public void startAnd() {
		// Not yet needed
	}

	@Override
	public void and() {
		// Not yet needed
	}

	@Override
	public void endAnd() {
		// Not yet needed
	}

	@Override
	public void startOr() {
		// Not yet needed
	}

	@Override
	public void or() {
		// Not yet needed
	}

	@Override
	public void endOr() {
		// Not yet needed
	}

	@Override
	public void startNegate() {
		// Not yet needed
	}

	@Override
	public void endNegate() {
		// Not yet needed
	}

	@Override
	public void unknown(RequestPredicate predicate) {
		// Not yet needed
	}

}


