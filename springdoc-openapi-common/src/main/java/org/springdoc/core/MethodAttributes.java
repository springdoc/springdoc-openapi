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

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.ArrayUtils;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class MethodAttributes {

	private boolean methodOverloaded;

	private boolean withApiResponseDoc;

	private JsonView jsonViewAnnotation;

	private JsonView jsonViewAnnotationForRequestBody;

	private String defaultConsumesMediaType;

	private String defaultProducesMediaType;

	private LinkedHashMap<String, String> headers = new LinkedHashMap<>();

	private String[] classProduces;

	private String[] classConsumes;

	private String[] methodProduces = {};

	private String[] methodConsumes = {};

	private Map<String, ApiResponse> genericMapResponse = new LinkedHashMap<>();

	public MethodAttributes(String[] methodProducesNew, String defaultConsumesMediaType, String defaultProducesMediaType, Map<String, ApiResponse> genericMapResponse) {
		this.methodProduces = methodProducesNew;
		this.defaultConsumesMediaType = defaultConsumesMediaType;
		this.defaultProducesMediaType = defaultProducesMediaType;
		this.genericMapResponse = genericMapResponse;
	}

	public MethodAttributes(String defaultConsumesMediaType, String defaultProducesMediaType) {
		this.defaultConsumesMediaType = defaultConsumesMediaType;
		this.defaultProducesMediaType = defaultProducesMediaType;
	}

	public MethodAttributes(String defaultConsumesMediaType, String defaultProducesMediaType, String[] methodConsumes, String[] methodProduces, String[] headers) {
		this.defaultConsumesMediaType = defaultConsumesMediaType;
		this.defaultProducesMediaType = defaultProducesMediaType;
		this.methodProduces = methodProduces;
		this.methodConsumes = methodConsumes;
		setHeaders(headers);
	}

	public String[] getClassProduces() {
		return classProduces;
	}

	public void setClassProduces(String[] classProduces) {
		this.classProduces = classProduces;
	}

	public String[] getClassConsumes() {
		return classConsumes;
	}

	public void setClassConsumes(String[] classConsumes) {
		this.classConsumes = classConsumes;
	}

	public String[] getMethodProduces() {
		return methodProduces;
	}

	public String[] getMethodConsumes() {
		return methodConsumes;
	}


	public void calculateConsumesProduces(Method method) {
		PostMapping reqPostMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, PostMapping.class);
		if (reqPostMappingMethod != null) {
			fillMethods(reqPostMappingMethod.produces(), reqPostMappingMethod.consumes(), reqPostMappingMethod.headers());
			return;
		}
		GetMapping reqGetMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, GetMapping.class);
		if (reqGetMappingMethod != null) {
			fillMethods(reqGetMappingMethod.produces(), reqGetMappingMethod.consumes(), reqGetMappingMethod.headers());
			return;
		}
		DeleteMapping reqDeleteMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, DeleteMapping.class);
		if (reqDeleteMappingMethod != null) {
			fillMethods(reqDeleteMappingMethod.produces(), reqDeleteMappingMethod.consumes(), reqDeleteMappingMethod.headers());
			return;
		}
		PutMapping reqPutMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, PutMapping.class);
		if (reqPutMappingMethod != null) {
			fillMethods(reqPutMappingMethod.produces(), reqPutMappingMethod.consumes(), reqPutMappingMethod.headers());
			return;
		}
		RequestMapping reqMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
		RequestMapping reqMappingClass = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), RequestMapping.class);

		if (reqMappingMethod != null && reqMappingClass != null) {
			fillMethods(ArrayUtils.addAll(reqMappingMethod.produces(), reqMappingClass.produces()), ArrayUtils.addAll(reqMappingMethod.consumes(), reqMappingClass.consumes()), reqMappingMethod.headers());
		}
		else if (reqMappingMethod != null) {
			fillMethods(reqMappingMethod.produces(), reqMappingMethod.consumes(), reqMappingMethod.headers());
		}
		else if (reqMappingClass != null) {
			fillMethods(reqMappingClass.produces(), reqMappingClass.consumes(), reqMappingClass.headers());
		}
		else
			fillMethods(methodProduces, methodConsumes, null);
	}

	private void fillMethods(String[] produces, String[] consumes, String[] headers) {
		if (ArrayUtils.isNotEmpty(produces))
			methodProduces = produces;
		else if (ArrayUtils.isNotEmpty(classProduces))
			methodProduces = classProduces;
		else
			methodProduces = new String[] { defaultProducesMediaType };

		if (ArrayUtils.isNotEmpty(consumes))
			methodConsumes = consumes;
		else if (ArrayUtils.isNotEmpty(classConsumes))
			methodConsumes = classConsumes;
		else
			methodConsumes = new String[] { defaultConsumesMediaType };

		setHeaders(headers);
	}

	private void setHeaders(String[] headers) {
		if (ArrayUtils.isNotEmpty(headers))
			for (String header : headers) {
				String[] keyValueHeader = header.split("=");
				String headerValue = keyValueHeader.length > 1 ? keyValueHeader[1] : "";
				this.headers.put(keyValueHeader[0], headerValue);
			}
	}

	public boolean isMethodOverloaded() {
		return methodOverloaded;
	}

	public void setMethodOverloaded(boolean overloaded) {
		methodOverloaded = overloaded;
	}

	public void setWithApiResponseDoc(boolean withApiDoc) {
		this.withApiResponseDoc = withApiDoc;
	}

	public boolean isNoApiResponseDoc() {
		return !withApiResponseDoc;
	}

	public JsonView getJsonViewAnnotation() {
		return jsonViewAnnotation;
	}

	public void setJsonViewAnnotation(JsonView jsonViewAnnotation) {
		this.jsonViewAnnotation = jsonViewAnnotation;
	}

	public JsonView getJsonViewAnnotationForRequestBody() {
		if (jsonViewAnnotationForRequestBody == null)
			return jsonViewAnnotation;
		return jsonViewAnnotationForRequestBody;
	}

	public void setJsonViewAnnotationForRequestBody(JsonView jsonViewAnnotationForRequestBody) {
		this.jsonViewAnnotationForRequestBody = jsonViewAnnotationForRequestBody;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public ApiResponses calculateGenericMapResponse(Map<String, ApiResponse> genericMapResponse) {
		ApiResponses apiResponses = new ApiResponses();
		genericMapResponse.forEach(apiResponses::addApiResponse);
		this.genericMapResponse = genericMapResponse;
		return apiResponses;
	}

	public Map<String, ApiResponse> getGenericMapResponse() {
		return genericMapResponse;
	}

}
