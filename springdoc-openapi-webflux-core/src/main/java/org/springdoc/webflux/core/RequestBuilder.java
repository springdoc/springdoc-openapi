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

package org.springdoc.webflux.core;

import java.util.List;
import java.util.Optional;

import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.GenericParameterBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.RequestBodyBuilder;
import org.springdoc.core.customizers.ParameterCustomizer;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import static org.springdoc.core.SpringDocUtils.getConfig;

/**
 * The type Request builder.
 * @author bnasslahsen
 */
public class RequestBuilder extends AbstractRequestBuilder {

	static {
		getConfig().addRequestWrapperToIgnore(ServerWebExchange.class, ServerHttpRequest.class)
				.addRequestWrapperToIgnore(ServerHttpResponse.class)
				.addRequestWrapperToIgnore(ServerRequest.class)
				.addFileType(FilePart.class);
	}

	/**
	 * Instantiates a new Request builder.
	 *
	 * @param parameterBuilder the parameter builder 
	 * @param requestBodyBuilder the request body builder 
	 * @param operationBuilder the operation builder 
	 * @param parameterCustomizers the parameter customizers 
	 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer
	 */
	public RequestBuilder(GenericParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
			OperationBuilder operationBuilder, Optional<List<ParameterCustomizer>> parameterCustomizers,
			LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
		super(parameterBuilder, requestBodyBuilder, operationBuilder, parameterCustomizers, localSpringDocParameterNameDiscoverer);
	}
}
