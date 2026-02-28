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

package org.springdoc.webmvc.core.service;

import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.discoverer.SpringDocParameterNameDiscoverer;
import org.springdoc.core.extractor.MethodParameterPojoExtractor;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericParameterService;
import org.springdoc.core.service.RequestBodyService;

import org.springframework.web.servlet.function.ServerRequest;

import static org.springdoc.core.utils.SpringDocUtils.getConfig;

/**
 * The type Request builder.
 *
 * @author bnasslahsen
 */
public class RequestService extends AbstractRequestService {

	static {
		getConfig().addRequestWrapperToIgnore(jakarta.servlet.ServletRequest.class)
				.addRequestWrapperToIgnore(jakarta.servlet.ServletResponse.class)
				.addRequestWrapperToIgnore(jakarta.servlet.http.HttpServletRequest.class)
				.addRequestWrapperToIgnore(jakarta.servlet.http.HttpServletResponse.class)
				.addRequestWrapperToIgnore(jakarta.servlet.http.HttpSession.class)
				.addRequestWrapperToIgnore(jakarta.servlet.http.HttpSession.class)
				.addRequestWrapperToIgnore(ServerRequest.class);
	}

	/**
	 * Instantiates a new Request builder.
	 *
	 * @param parameterBuilder                      the parameter builder
	 * @param requestBodyService                    the request body builder
	 * @param springDocCustomizers                  the spring doc customizers
	 * @param localSpringDocParameterNameDiscoverer the local spring doc parameter name discoverer
	 * @param methodParameterPojoExtractor          the method parameter pojo extractor
	 */
	public RequestService(GenericParameterService parameterBuilder, RequestBodyService requestBodyService,
			SpringDocCustomizers springDocCustomizers,
			SpringDocParameterNameDiscoverer localSpringDocParameterNameDiscoverer,
			MethodParameterPojoExtractor methodParameterPojoExtractor) {
		super(parameterBuilder, requestBodyService, springDocCustomizers, localSpringDocParameterNameDiscoverer,methodParameterPojoExtractor);
	}
}
