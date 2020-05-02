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
