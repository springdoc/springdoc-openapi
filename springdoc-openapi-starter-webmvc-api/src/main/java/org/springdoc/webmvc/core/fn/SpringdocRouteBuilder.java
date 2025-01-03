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

package org.springdoc.webmvc.core.fn;


import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.springdoc.core.fn.AbstractSpringdocRouteBuilder;
import org.springdoc.core.fn.builders.operation.Builder;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springdoc.core.utils.Constants.OPERATION_ATTRIBUTE;

/**
 * The type Springdoc route builder.
 *
 * @author bnasslahsen
 */
public class SpringdocRouteBuilder extends AbstractSpringdocRouteBuilder {

	/**
	 * The Delegate.
	 */
	private final RouterFunctions.Builder delegate = RouterFunctions.route();

	/**
	 * Instantiates a new Springdoc route builder.
	 */
	private SpringdocRouteBuilder() {
	}

	/**
	 * Route springdoc route builder.
	 *
	 * @return the springdoc route builder
	 */
	public static SpringdocRouteBuilder route() {
		return new SpringdocRouteBuilder();
	}

	/**
	 * Build router function.
	 *
	 * @return the router function
	 */
	public RouterFunction<ServerResponse> build() {
		return this.delegate.build();
	}

	/**
	 * Get springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder GET(String pattern, HandlerFunction<ServerResponse> handlerFunction,
			Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.GET(pattern, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}

	/**
	 * Get springdoc route builder.
	 *
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder GET(HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.GET(handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Get springdoc route builder.
	 *
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder GET(RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.GET(predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Get springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder GET(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.GET(pattern, predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Head springdoc route builder.
	 *
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder HEAD(HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.HEAD(handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Head springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder HEAD(String pattern, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.HEAD(pattern, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Head springdoc route builder.
	 *
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder HEAD(RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.HEAD(predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Head springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder HEAD(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.HEAD(pattern, predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Post springdoc route builder.
	 *
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder POST(HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.POST(handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Post springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder POST(String pattern, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.POST(pattern, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Post springdoc route builder.
	 *
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder POST(RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.POST(predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Post springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder POST(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.POST(pattern, predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Put springdoc route builder.
	 *
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder PUT(HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.PUT(handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Put springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder PUT(String pattern, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.PUT(pattern, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Put springdoc route builder.
	 *
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder PUT(RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.PUT(predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Put springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder PUT(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.PUT(pattern, predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Patch springdoc route builder.
	 *
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder PATCH(HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.PATCH(handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Patch springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder PATCH(String pattern, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.PATCH(pattern, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Patch springdoc route builder.
	 *
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder PATCH(RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.PATCH(predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Patch springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder PATCH(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.PATCH(pattern, predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Delete springdoc route builder.
	 *
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder DELETE(HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.DELETE(handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Delete springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder DELETE(String pattern, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.DELETE(pattern, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Delete springdoc route builder.
	 *
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder DELETE(RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.DELETE(predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Delete springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder DELETE(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.DELETE(pattern, predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Options springdoc route builder.
	 *
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder OPTIONS(HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.OPTIONS(handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Options springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder OPTIONS(String pattern, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.OPTIONS(pattern, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Options springdoc route builder.
	 *
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder OPTIONS(RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.OPTIONS(predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Options springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param predicate          the predicate
	 * @param handlerFunction    the handler function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder OPTIONS(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.OPTIONS(pattern, predicate, handlerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}

	/**
	 * Add springdoc route builder.
	 *
	 * @param routerFunction     the router function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder add(RouterFunction<ServerResponse> routerFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.add(routerFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Resources springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param location           the location
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder resources(String pattern, Resource location, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.resources(pattern, location).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Resources springdoc route builder.
	 *
	 * @param lookupFunction     the lookup function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder resources(Function<ServerRequest, Optional<Resource>> lookupFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.resources(lookupFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Nest springdoc route builder.
	 *
	 * @param predicate              the predicate
	 * @param routerFunctionSupplier the router function supplier
	 * @param operationsConsumer     the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder nest(RequestPredicate predicate, Supplier<RouterFunction<ServerResponse>> routerFunctionSupplier, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.nest(predicate, routerFunctionSupplier).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Nest springdoc route builder.
	 *
	 * @param predicate          the predicate
	 * @param builderConsumer    the builder consumer
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder nest(RequestPredicate predicate, Consumer<RouterFunctions.Builder> builderConsumer, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.nest(predicate, builderConsumer).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Path springdoc route builder.
	 *
	 * @param pattern                the pattern
	 * @param routerFunctionSupplier the router function supplier
	 * @param operationsConsumer     the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder path(String pattern, Supplier<RouterFunction<ServerResponse>> routerFunctionSupplier, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.path(pattern, routerFunctionSupplier).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Path springdoc route builder.
	 *
	 * @param pattern            the pattern
	 * @param builderConsumer    the builder consumer
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder path(String pattern, Consumer<RouterFunctions.Builder> builderConsumer, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.path(pattern, builderConsumer).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Filter springdoc route builder.
	 *
	 * @param filterFunction     the filter function
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder filter(HandlerFilterFunction<ServerResponse, ServerResponse> filterFunction, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.filter(filterFunction).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * Before springdoc route builder.
	 *
	 * @param requestProcessor   the request processor
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder before(UnaryOperator<ServerRequest> requestProcessor, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.before(requestProcessor).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * After springdoc route builder.
	 *
	 * @param responseProcessor  the response processor
	 * @param operationsConsumer the operations consumer
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder after(BiFunction<ServerRequest, ServerResponse, ServerResponse> responseProcessor, Consumer<Builder> operationsConsumer) {
		Builder builder = getOperationBuilder(operationsConsumer);
		this.delegate.after(responseProcessor).withAttribute(OPERATION_ATTRIBUTE, builder);
		return this;
	}


	/**
	 * On error springdoc route builder.
	 *
	 * @param predicate        the predicate
	 * @param responseProvider the response provider
	 * @return the springdoc route builder
	 */
	public SpringdocRouteBuilder onError(Predicate<Throwable> predicate, BiFunction<Throwable, ServerRequest, ServerResponse> responseProvider) {
		this.delegate.onError(predicate, responseProvider);
		return this;
	}


	/**
	 * On error springdoc route builder.
	 *
	 * @param <T>              the type parameter
	 * @param exceptionType    the exception type
	 * @param responseProvider the response provider
	 * @return the springdoc route builder
	 */
	public <T extends Throwable> SpringdocRouteBuilder onError(Class<T> exceptionType, BiFunction<Throwable, ServerRequest, ServerResponse> responseProvider) {
		this.delegate.onError(exceptionType, responseProvider);
		return this;
	}

}