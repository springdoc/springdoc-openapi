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

package org.springdoc.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

public class RouterFunctionData {

	private String path;

	private List<String> consumes = new ArrayList<>();

	private List<String> produces = new ArrayList<>();

	private List<String> headers = new ArrayList<>();

	private String queryParam;

	private RequestMethod[] methods;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

	public String[] getHeaders() {
		return headers.toArray(new String[headers.size()]);
	}

	public void addHeaders(String headers) {
		if (StringUtils.isNotBlank(headers))
			this.headers.add(headers);
	}

	public RequestMethod[] getMethods() {
		return methods;
	}

	public void setMethods(Set<HttpMethod> methods) {
		this.methods = getMethod(methods);
	}

	public String[] getConsumes() {
		return consumes.toArray(new String[consumes.size()]);
	}

	public void addConsumes(String consumes) {
		if (StringUtils.isNotBlank(consumes))
			this.consumes.add(consumes);
	}

	public void addProduces(String produces) {
		if (StringUtils.isNotBlank(produces))
			 this.produces.add(produces);
	}

	private RequestMethod[] getMethod(Set<HttpMethod> methods) {
		if (!CollectionUtils.isEmpty(methods)) {
			return methods.stream().map(this::getRequestMethod).toArray(RequestMethod[]::new);
		}
		return ArrayUtils.toArray();
	}

	public String[] getProduces() {
		return produces.toArray(new String[produces.size()]);
	}

	private RequestMethod getRequestMethod(HttpMethod httpMethod) {
		RequestMethod requestMethod = null;
		switch (httpMethod) {
			case GET:
				requestMethod = RequestMethod.GET;
				break;
			case POST:
				requestMethod = RequestMethod.POST;
				break;
			case PUT:
				requestMethod = RequestMethod.PUT;
				break;
			case DELETE:
				requestMethod = RequestMethod.DELETE;
				break;
			case PATCH:
				requestMethod = RequestMethod.PATCH;
				break;
			case HEAD:
				requestMethod = RequestMethod.HEAD;
				break;
			case OPTIONS:
				requestMethod = RequestMethod.OPTIONS;
				break;
			default:
				// Do nothing here
				break;
		}
		return requestMethod;
	}
}
