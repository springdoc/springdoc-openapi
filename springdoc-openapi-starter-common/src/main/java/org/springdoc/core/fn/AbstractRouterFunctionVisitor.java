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

package org.springdoc.core.fn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

/**
 * The type Abstract router function visitor.
 *
 * @author bnasslahsen
 */
public class AbstractRouterFunctionVisitor {

	/**
	 * The Consumes.
	 */
	private final List<String> consumes = new ArrayList<>();

	/**
	 * The Produces.
	 */
	private final List<String> produces = new ArrayList<>();

	/**
	 * The Query params.
	 */
	private final Map<String, String> queryParams = new LinkedHashMap<>();

	/**
	 * The Router function datas.
	 */
	protected List<RouterFunctionData> routerFunctionDatas = new ArrayList<>();

	/**
	 * The Nested or paths.
	 */
	protected List<String> orPaths = new ArrayList<>();

	/**
	 * The Nested and paths.
	 */
	protected Map<Integer, List<String>> nestedPaths = new LinkedHashMap<>();

	/**
	 * The Is or.
	 */
	protected boolean isOr;

	/**
	 * The Router function data.
	 */
	protected List<RouterFunctionData> currentRouterFunctionDatas;

	/**
	 * The Attributes.
	 */
	protected Map<String, Object> attributes = new LinkedHashMap<>();

	/**
	 * The Level.
	 */
	private int level;

	/**
	 * The Methods.
	 */
	private Set<HttpMethod> methods;

	/**
	 * Method.
	 *
	 * @param methods the methods
	 */
	public void method(Set<HttpMethod> methods) {
		if (CollectionUtils.isEmpty(currentRouterFunctionDatas))
			this.methods = methods;
		else
			currentRouterFunctionDatas.forEach(routerFunctionData -> routerFunctionData.setMethods(methods));
	}

	/**
	 * Path.
	 *
	 * @param pattern the pattern
	 */
	public void path(String pattern) {
		if (currentRouterFunctionDatas != null) {
			if (!nestedPaths.isEmpty()) {
				List<String> nestedPathsList = this.nestedPaths.values().stream().flatMap(List::stream).toList();
				if (!orPaths.isEmpty())
					orPaths.forEach(nestedOrPath -> createRouterFunctionData(String.join(StringUtils.EMPTY, nestedPathsList) + nestedOrPath + pattern));
				else
					createRouterFunctionData(String.join(StringUtils.EMPTY, nestedPathsList) + pattern);
			}
			else if (!orPaths.isEmpty())
				orPaths.forEach(nestedOrPath -> createRouterFunctionData(nestedOrPath + pattern));
			else
				createRouterFunctionData(pattern);
		}
		else if (isOr)
			orPaths.add(pattern);
		else if (this.level > 0) {
			List<String> paths = CollectionUtils.isEmpty(this.nestedPaths.get(this.level)) ? new ArrayList<>() : this.nestedPaths.get(this.level);
			paths.add(pattern);
			this.nestedPaths.put(this.level, paths);
		}
	}

	/**
	 * Header.
	 *
	 * @param name  the name
	 * @param value the value
	 */
	public void header(String name, String value) {
		if (HttpHeaders.ACCEPT.equals(name))
			calculateHeader(value, this.produces, name);
		else if (HttpHeaders.CONTENT_TYPE.equals(name))
			calculateHeader(value, this.consumes, name);
		else
			currentRouterFunctionDatas.forEach(routerFunctionData -> routerFunctionData.addHeaders(name + "=" + value));
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
	 * @param name  the name
	 * @param value the value
	 */
	public void queryParam(String name, String value) {
		if (CollectionUtils.isEmpty(currentRouterFunctionDatas))
			queryParams.put(name, value);
		else
			currentRouterFunctionDatas.forEach(routerFunctionData -> routerFunctionData.addQueryParams(name, value));
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
	 * @param name  the name
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
		this.isOr = false;
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
	 * Attributes.
	 *
	 * @param map the map
	 */
	public void attributes(Map<String, Object> map) {
		this.attributes = map;
	}

	/**
	 * Compute nested.
	 */
	protected void commonEndNested() {
		nestedPaths.remove(this.level);
		this.level--;
	}

	/**
	 * Common start nested.
	 */
	protected void commonStartNested() {
		this.level++;
		this.currentRouterFunctionDatas = null;
	}

	/**
	 * Common route.
	 */
	protected void commonRoute() {
		this.routerFunctionDatas.addAll(currentRouterFunctionDatas);
		currentRouterFunctionDatas.forEach(routerFunctionData -> routerFunctionData.addAttributes(this.attributes));
		this.attributes = new HashMap<>();
	}

	/**
	 * Calculate header.
	 *
	 * @param value   the value
	 * @param headers the headers
	 * @param header  the header
	 */
	private void calculateHeader(String value, List<String> headers, String header) {
		if (value.contains(",")) {
			String[] mediaTypes = value.substring(1, value.length() - 1).split(", ");
			for (String mediaType : mediaTypes)
				if (CollectionUtils.isEmpty(currentRouterFunctionDatas))
					headers.add(mediaType);
				else
					currentRouterFunctionDatas.forEach(routerFunctionData -> addHeader(mediaType, header, routerFunctionData));
		}
		else {
			if (CollectionUtils.isEmpty(currentRouterFunctionDatas))
				headers.add(value);
			else
				currentRouterFunctionDatas.forEach(routerFunctionData -> addHeader(value, header, routerFunctionData));
		}
	}

	/**
	 * Create router function data.
	 *
	 * @param path the path
	 */
	private void createRouterFunctionData(String path) {
		RouterFunctionData routerFunctionData = new RouterFunctionData();
		routerFunctionData.setPath(path);
		routerFunctionData.setMethods(methods);
		routerFunctionData.addConsumes(consumes);
		routerFunctionData.addProduces(produces);
		this.queryParams.forEach(routerFunctionData::addQueryParams);
		this.currentRouterFunctionDatas.add(routerFunctionData);
	}

	/**
	 * Add header.
	 *
	 * @param mediaType          the media type
	 * @param header             the header
	 * @param routerFunctionData the router function data
	 */
	private void addHeader(String mediaType, String header, RouterFunctionData routerFunctionData) {
		if (HttpHeaders.CONTENT_TYPE.equals(header))
			routerFunctionData.addConsumes(mediaType);
		else if (HttpHeaders.ACCEPT.equals(header))
			routerFunctionData.addProduces(mediaType);
	}
}
