/*
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springdoc.core;

import java.util.List;
import java.util.Optional;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.ParameterCustomizer;

import static org.springdoc.core.SpringDocUtils.getConfig;

public class RequestBuilder extends AbstractRequestBuilder {

	static {
		getConfig().addRequestWrapperToIgnore(javax.servlet.ServletRequest.class)
		.addRequestWrapperToIgnore(javax.servlet.ServletResponse.class)
		.addRequestWrapperToIgnore(javax.servlet.http.HttpServletRequest.class)
		.addRequestWrapperToIgnore(javax.servlet.http.HttpServletResponse.class)
		.addRequestWrapperToIgnore(javax.servlet.http.HttpSession.class)
		.addRequestWrapperToIgnore(javax.servlet.http.HttpSession.class);
	}

	public RequestBuilder(GenericParameterBuilder parameterBuilder, RequestBodyBuilder requestBodyBuilder,
			OperationBuilder operationBuilder, Optional<List<OperationCustomizer>> customizers,
			Optional<List<ParameterCustomizer>> parameterCustomizers) {
		super(parameterBuilder, requestBodyBuilder, operationBuilder, customizers, parameterCustomizers);
	}
}
