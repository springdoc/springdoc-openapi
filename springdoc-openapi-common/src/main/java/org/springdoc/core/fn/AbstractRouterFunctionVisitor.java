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

package org.springdoc.core.fn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * The type Abstract router function visitor.
 * @author bnasslahsen
 */
public class AbstractRouterFunctionVisitor {

	/**
	 * The Router function datas.
	 */
	protected List<RouterFunctionData> routerFunctionDatas = new ArrayList<>();

	/**
	 * The Nested or paths.
	 */
	protected List<String> nestedOrPaths = new ArrayList<>();

	/**
	 * The Nested and paths.
	 */
	protected List<String> nestedAndPaths = new ArrayList<>();

	/**
	 * The Nested accept headers.
	 */
	protected List<String> nestedAcceptHeaders = new ArrayList<>();

	/**
	 * The Nested content type headers.
	 */
	protected List<String> nestedContentTypeHeaders = new ArrayList<>();

	/**
	 * The Is or.
	 */
	protected boolean isOr;

	/**
	 * The Is nested.
	 */
	protected boolean isNested;

	/**
	 * The Router function data.
	 */
	protected RouterFunctionData routerFunctionData;

	/**
	 * The Attributes.
	 */
	protected Map<String,Object> attributes = new LinkedHashMap<>();

	/**
	 * Method.
	 *
	 * @param methods the methods
	 */
	public void method(Set<HttpMethod> methods) {
		routerFunctionData.setMethods(methods);
	}

	/**
	 * Path.
	 *
	 * @param pattern the pattern
	 */
	public void path(String pattern) {
		if (routerFunctionData != null)
			routerFunctionData.setPath(pattern);
		else if (isOr)
			nestedOrPaths.add(pattern);
		else if (isNested)
			nestedAndPaths.add(pattern);
	}

	/**
	 * Header.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public void header(String name, String value) {
		if (HttpHeaders.ACCEPT.equals(name)) {
			calculateAccept(value);
		}
		else if (HttpHeaders.CONTENT_TYPE.equals(name)) {
			calculateContentType(value);
		}
		else
			routerFunctionData.addHeaders(name + "=" + value);
	}

	/**
	 * Gets router function datas.
	 *
	 * @return the router function datas
	 */
	public List<RouterFunctionData> getRouterFunctionDatas() {
		return routerFunctionDatas;
	}

	/**
	 * Query param.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public void queryParam(String name, String value) {
		routerFunctionData.addQueryParams(name, value);
	}

	/**
	 * Path extension.
	 *
	 * @param extension the extension
	 */
	public void pathExtension(String extension) {
		// Not yet needed
	}

	/**
	 * Param.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public void param(String name, String value) {
		// Not yet needed
	}

	/**
	 * Start and.
	 */
	public void startAnd() {
		// Not yet needed
	}

	/**
	 * And.
	 */
	public void and() {
		// Not yet needed
	}

	/**
	 * End and.
	 */
	public void endAnd() {
		// Not yet needed
	}

	/**
	 * Start or.
	 */
	public void startOr() {
		isOr = true;
	}

	/**
	 * Or.
	 */
	public void or() {
		// Not yet needed
	}

	/**
	 * End or.
	 */
	public void endOr() {
		// Not yet needed
	}

	/**
	 * Start negate.
	 */
	public void startNegate() {
		// Not yet needed
	}

	/**
	 * End negate.
	 */
	public void endNegate() {
		// Not yet needed
	}

	/**
	 * Compute nested.
	 */
	protected void computeNested() {
		if (!nestedAndPaths.isEmpty()) {
			String nestedPath = String.join(StringUtils.EMPTY, nestedAndPaths);
			routerFunctionDatas.forEach(existingRouterFunctionData -> existingRouterFunctionData.setPath(nestedPath + existingRouterFunctionData.getPath()));
			nestedAndPaths.clear();
		}
		if (!nestedOrPaths.isEmpty()) {
			List<RouterFunctionData> routerFunctionDatasClone = new ArrayList<>();
			for (RouterFunctionData functionData : routerFunctionDatas) {
				for (String nestedOrPath : nestedOrPaths) {
					RouterFunctionData routerFunctionDataClone = new RouterFunctionData(nestedOrPath , functionData);
					routerFunctionDatasClone.add(routerFunctionDataClone);
				}
			}
			this.routerFunctionDatas = routerFunctionDatasClone;
			nestedAndPaths.clear();
		}
		if (!nestedAcceptHeaders.isEmpty()) {
			routerFunctionDatas.forEach(existingRouterFunctionData -> existingRouterFunctionData.addProduces(nestedAcceptHeaders));
			nestedAcceptHeaders.clear();
		}
		if (!nestedContentTypeHeaders.isEmpty()) {
			routerFunctionDatas.forEach(existingRouterFunctionData -> existingRouterFunctionData.addConsumes(nestedContentTypeHeaders));
			nestedContentTypeHeaders.clear();
		}
	}

	/**
	 * Calculate content type.
	 *
	 * @param value the value
	 */
	private void calculateContentType(String value) {
		if (value.contains(",")) {
			String[] mediaTypes = value.substring(1, value.length() - 1).split(", ");
			for (String mediaType : mediaTypes)
				if (routerFunctionData != null)
					routerFunctionData.addConsumes(mediaType);
				else
					nestedContentTypeHeaders.addAll(Arrays.asList(mediaTypes));
		}
		else {
			if (routerFunctionData != null)
				routerFunctionData.addConsumes(value);
			else
				nestedContentTypeHeaders.add(value);
		}
	}

	/**
	 * Calculate accept.
	 *
	 * @param value the value
	 */
	private void calculateAccept(String value) {
		if (value.contains(",")) {
			String[] mediaTypes = value.substring(1, value.length() - 1).split(", ");
			for (String mediaType : mediaTypes)
				if (routerFunctionData != null)
					routerFunctionData.addProduces(mediaType);
				else
					nestedAcceptHeaders.addAll(Arrays.asList(mediaTypes));
		}
		else {
			if (routerFunctionData != null)
				routerFunctionData.addProduces(value);
			else
				nestedAcceptHeaders.add(value);
		}
	}

	/**
	 * Route.
	 */
	protected void route() {
		this.routerFunctionData = new RouterFunctionData();
		routerFunctionDatas.add(this.routerFunctionData);
		this.routerFunctionData.addAttributes(this.attributes);
	}

	/**
	 * Attributes.
	 *
	 * @param map the map
	 */
	public void attributes(Map<String, Object> map) {
		this.attributes = map;
	}

}
