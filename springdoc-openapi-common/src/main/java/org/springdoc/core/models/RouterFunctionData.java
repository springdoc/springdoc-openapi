package org.springdoc.core.models;

import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

public class RouterFunctionData {

	private String path;

	private String[] consumes;

	private String[] headers;

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
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	public void setHeaders(String headers) {
		if (StringUtils.isNotBlank(headers))
			this.headers = new String[] { headers };
	}

	public RequestMethod[] getMethods() {
		return methods;
	}

	public void setMethods(RequestMethod[] methods) {
		this.methods = methods;
	}

	public void setMethods(Set<HttpMethod> methods) {
		this.methods = getMethod(methods);
	}

	public String[] getConsumes() {
		return consumes;
	}

	public void setConsumes(String[] consumes) {
		this.consumes = consumes;
	}

	public void setConsumes(String consumes) {
		if (StringUtils.isNotBlank(consumes))
			this.consumes = new String[] { consumes };
	}
	private RequestMethod[] getMethod(Set<HttpMethod> methods) {
		if (!CollectionUtils.isEmpty(methods)) {
			return methods.stream().map(this::getRequestMethod).toArray(RequestMethod[]::new);
		}
		return ArrayUtils.toArray();
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
