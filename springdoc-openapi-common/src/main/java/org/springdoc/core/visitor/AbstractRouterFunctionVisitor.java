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

package org.springdoc.core.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springdoc.core.models.RouterFunctionData;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class AbstractRouterFunctionVisitor {

	protected List<RouterFunctionData> routerFunctionDatas = new ArrayList<>();

	protected RouterFunctionData routerFunctionData;

	public void method(Set<HttpMethod> methods) {
		routerFunctionData.setMethods(methods);
	}

	public void path(String pattern) {
		routerFunctionData.setPath(pattern);
	}

	public void header(String name, String value) {
		if (HttpHeaders.ACCEPT.equals(name))
			routerFunctionData.setProduces(value);
		else if (HttpHeaders.CONTENT_TYPE.equals(name))
			routerFunctionData.setConsumes(value);
		else
			routerFunctionData.setHeaders(name + "=" + value);
	}

	public List<RouterFunctionData> getRouterFunctionDatas() {
		return routerFunctionDatas;
	}

	public void queryParam(String name, String value) {
		// Not yet needed
	}

	public void pathExtension(String extension) {
		// Not yet needed
	}

	public void param(String name, String value) {
		// Not yet needed
	}

	public void startAnd() {
		// Not yet needed
	}

	public void and() {
		// Not yet needed
	}

	public void endAnd() {
		// Not yet needed
	}

	public void startOr() {
		// Not yet needed
	}

	public void or() {
		// Not yet needed
	}

	public void endOr() {
		// Not yet needed
	}

	public void startNegate() {
		// Not yet needed
	}

	public void endNegate() {
		// Not yet needed
	}




}
