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

package org.springdoc.webflux.core.service;

import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.discoverer.SpringDocParameterNameDiscoverer;
import org.springdoc.core.extractor.MethodParameterPojoExtractor;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericParameterService;
import org.springdoc.core.service.RequestBodyService;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import static org.springdoc.core.utils.SpringDocUtils.getConfig;

/**
 * The type Request builder.
 *
 * @author bnasslahsen
 */
public class RequestService extends AbstractRequestService {

	static {
		getConfig().addRequestWrapperToIgnore(ServerWebExchange.class, ServerHttpRequest.class)
				.addRequestWrapperToIgnore(ServerHttpResponse.class)
				.addRequestWrapperToIgnore(ServerRequest.class)
				.addFileType(FilePart.class);
	}

	/**
	 * Instantiates a new Request builder.
	 *
	 * @param parameterBuilder                      the parameter builder
	 * @param requestBodyService                    the request body builder
	 * @param customizers                           the parameter customizers
	 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer
	 * @param methodParameterPojoExtractor          the method parameter pojo extractor
	 */
	public RequestService(GenericParameterService parameterBuilder, RequestBodyService requestBodyService,
			SpringDocCustomizers customizers,
			SpringDocParameterNameDiscoverer localSpringDocParameterNameDiscoverer,
			MethodParameterPojoExtractor methodParameterPojoExtractor) {
		super(parameterBuilder, requestBodyService, customizers, localSpringDocParameterNameDiscoverer,methodParameterPojoExtractor);
	}
}
