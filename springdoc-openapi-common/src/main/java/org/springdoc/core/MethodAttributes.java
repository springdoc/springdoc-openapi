/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Method attributes.
 * @author bnasslahsen
 */
public class MethodAttributes {

	/**
	 * The Default consumes media type.
	 */
	private final String defaultConsumesMediaType;

	/**
	 * The Default produces media type.
	 */
	private final String defaultProducesMediaType;

	/**
	 * The Headers.
	 */
	private final LinkedHashMap<String, String> headers = new LinkedHashMap<>();

	/**
	 * The Locale.
	 */
	private final Locale locale;

	/**
	 * The Method overloaded.
	 */
	private boolean methodOverloaded;

	/**
	 * The With api response doc.
	 */
	private boolean withApiResponseDoc;

	/**
	 * The With response body schema doc.
	 */
	private boolean withResponseBodySchemaDoc;

	/**
	 * The Json view annotation.
	 */
	private JsonView jsonViewAnnotation;

	/**
	 * The Json view annotation for request body.
	 */
	private JsonView jsonViewAnnotationForRequestBody;

	/**
	 * The Class produces.
	 */
	private String[] classProduces;

	/**
	 * The Class consumes.
	 */
	private String[] classConsumes;

	/**
	 * The Method produces.
	 */
	private String[] methodProduces = {};

	/**
	 * The Method consumes.
	 */
	private String[] methodConsumes = {};

	/**
	 * The Generic map response.
	 */
	private Map<String, ApiResponse> genericMapResponse = new LinkedHashMap<>();

	/**
	 * The javadoc Return.
	 */
	private String javadocReturn;

	/**
	 * Instantiates a new Method attributes.
	 * @param methodProducesNew the method produces new
	 * @param defaultConsumesMediaType the default consumes media type
	 * @param defaultProducesMediaType the default produces media type
	 * @param genericMapResponse the generic map response
	 * @param locale the locale
	 */
	public MethodAttributes(String[] methodProducesNew, String defaultConsumesMediaType, String defaultProducesMediaType, Map<String, ApiResponse> genericMapResponse, Locale locale) {
		this.methodProduces = methodProducesNew;
		this.defaultConsumesMediaType = defaultConsumesMediaType;
		this.defaultProducesMediaType = defaultProducesMediaType;
		this.genericMapResponse = genericMapResponse;
		this.locale = locale;
	}

	/**
	 * Instantiates a new Method attributes.
	 * @param defaultConsumesMediaType the default consumes media type
	 * @param defaultProducesMediaType the default produces media type
	 * @param locale the locale
	 */
	public MethodAttributes(String defaultConsumesMediaType, String defaultProducesMediaType, Locale locale) {
		this.defaultConsumesMediaType = defaultConsumesMediaType;
		this.defaultProducesMediaType = defaultProducesMediaType;
		this.locale = locale;
	}

	/**
	 * Instantiates a new Method attributes.
	 * @param defaultConsumesMediaType the default consumes media type
	 * @param defaultProducesMediaType the default produces media type
	 * @param methodConsumes the method consumes
	 * @param methodProduces the method produces
	 * @param headers the headers
	 * @param locale the locale
	 */
	public MethodAttributes(String defaultConsumesMediaType, String defaultProducesMediaType, String[] methodConsumes, String[] methodProduces, String[] headers, Locale locale) {
		this.defaultConsumesMediaType = defaultConsumesMediaType;
		this.defaultProducesMediaType = defaultProducesMediaType;
		this.methodProduces = methodProduces;
		this.methodConsumes = methodConsumes;
		this.locale = locale;
		setHeaders(headers);
	}

	/**
	 * Get class produces string [ ].
	 *
	 * @return the string [ ]
	 */
	public String[] getClassProduces() {
		return classProduces;
	}

	/**
	 * Sets class produces.
	 *
	 * @param classProduces the class produces
	 */
	public void setClassProduces(String[] classProduces) {
		this.classProduces = classProduces;
	}

	/**
	 * Get class consumes string [ ].
	 *
	 * @return the string [ ]
	 */
	public String[] getClassConsumes() {
		return classConsumes;
	}

	/**
	 * Sets class consumes.
	 *
	 * @param classConsumes the class consumes
	 */
	public void setClassConsumes(String[] classConsumes) {
		this.classConsumes = classConsumes;
	}

	/**
	 * Get method produces string [ ].
	 *
	 * @return the string [ ]
	 */
	public String[] getMethodProduces() {
		return methodProduces;
	}

	/**
	 * Get method consumes string [ ].
	 *
	 * @return the string [ ]
	 */
	public String[] getMethodConsumes() {
		return methodConsumes;
	}


	/**
	 * Calculate consumes produces.
	 *
	 * @param method the method
	 */
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

	/**
	 * Fill methods.
	 *
	 * @param produces the produces
	 * @param consumes the consumes
	 * @param headers the headers
	 */
	private void fillMethods(String[] produces, String[] consumes, String[] headers) {
		if (ArrayUtils.isEmpty(methodProduces)) {
			if (ArrayUtils.isNotEmpty(produces))
				methodProduces = produces;
			else if (ArrayUtils.isNotEmpty(classProduces))
				methodProduces = classProduces;
			else
				methodProduces = new String[] { defaultProducesMediaType };
		}

		if (ArrayUtils.isEmpty(methodConsumes)) {
			if (ArrayUtils.isNotEmpty(consumes))
				methodConsumes = consumes;
			else if (ArrayUtils.isNotEmpty(classConsumes))
				methodConsumes = classConsumes;
			else
				methodConsumes = new String[] { defaultConsumesMediaType };
		}

		if (CollectionUtils.isEmpty(this.headers))
			setHeaders(headers);
	}

	/**
	 * Is method overloaded boolean.
	 *
	 * @return the boolean
	 */
	public boolean isMethodOverloaded() {
		return methodOverloaded;
	}

	/**
	 * Sets method overloaded.
	 *
	 * @param overloaded the overloaded
	 */
	public void setMethodOverloaded(boolean overloaded) {
		methodOverloaded = overloaded;
	}

	/**
	 * Sets with api response doc.
	 *
	 * @param withApiDoc the with api doc
	 */
	public void setWithApiResponseDoc(boolean withApiDoc) {
		this.withApiResponseDoc = withApiDoc;
	}

	/**
	 * Is no api response doc boolean.
	 *
	 * @return the boolean
	 */
	public boolean isNoApiResponseDoc() {
		return !withApiResponseDoc;
	}

	/**
	 * Gets json view annotation.
	 *
	 * @return the json view annotation
	 */
	public JsonView getJsonViewAnnotation() {
		return jsonViewAnnotation;
	}

	/**
	 * Sets json view annotation.
	 *
	 * @param jsonViewAnnotation the json view annotation
	 */
	public void setJsonViewAnnotation(JsonView jsonViewAnnotation) {
		this.jsonViewAnnotation = jsonViewAnnotation;
	}

	/**
	 * Gets json view annotation for request body.
	 *
	 * @return the json view annotation for request body
	 */
	public JsonView getJsonViewAnnotationForRequestBody() {
		if (jsonViewAnnotationForRequestBody == null)
			return jsonViewAnnotation;
		return jsonViewAnnotationForRequestBody;
	}

	/**
	 * Sets json view annotation for request body.
	 *
	 * @param jsonViewAnnotationForRequestBody the json view annotation for request body
	 */
	public void setJsonViewAnnotationForRequestBody(JsonView jsonViewAnnotationForRequestBody) {
		this.jsonViewAnnotationForRequestBody = jsonViewAnnotationForRequestBody;
	}

	/**
	 * Gets headers.
	 *
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Sets headers.
	 *
	 * @param headers the headers
	 */
	private void setHeaders(String[] headers) {
		if (ArrayUtils.isNotEmpty(headers))
			for (String header : headers) {
				if (!header.contains("!=")) {
					String[] keyValueHeader = header.split("=");
					String headerValue = keyValueHeader.length > 1 ? keyValueHeader[1] : "";
					this.headers.put(keyValueHeader[0], headerValue);
				}
				else {
					String[] keyValueHeader = header.split("!=");
					if (!this.headers.containsKey(keyValueHeader[0]))
						this.headers.put(keyValueHeader[0], StringUtils.EMPTY);
				}
			}
	}

	/**
	 * Calculate generic map response api responses.
	 *
	 * @param genericMapResponse the generic map response
	 * @return the api responses
	 */
	public ApiResponses calculateGenericMapResponse(Map<String, ApiResponse> genericMapResponse) {
		ApiResponses apiResponses = new ApiResponses();
		genericMapResponse.forEach(apiResponses::addApiResponse);
		this.genericMapResponse = genericMapResponse;
		return apiResponses;
	}

	/**
	 * Gets generic map response.
	 *
	 * @return the generic map response
	 */
	public Map<String, ApiResponse> getGenericMapResponse() {
		return genericMapResponse;
	}

	/**
	 * Is with response body schema doc boolean.
	 *
	 * @return the boolean
	 */
	public boolean isWithResponseBodySchemaDoc() {
		return withResponseBodySchemaDoc;
	}

	/**
	 * Sets with response body schema doc.
	 *
	 * @param withResponseBodySchemaDoc the with response body schema doc
	 */
	public void setWithResponseBodySchemaDoc(boolean withResponseBodySchemaDoc) {
		this.withResponseBodySchemaDoc = withResponseBodySchemaDoc;
	}

	/**
	 * Calculate headers for class.
	 *
	 * @param declaringClass the declaring class
	 */
	public void calculateHeadersForClass(Class<?> declaringClass) {
		RequestMapping reqMappingClass = AnnotatedElementUtils.findMergedAnnotation(declaringClass, RequestMapping.class);
		if (reqMappingClass != null) {
			fillMethods(reqMappingClass.produces(), reqMappingClass.consumes(), reqMappingClass.headers());
		}
	}

	/**
	 * Gets javadoc return value
	 *
	 * @return the javadoc return
	 */
	public String getJavadocReturn() {
		return javadocReturn;
	}

	/**
	 * Sets javadoc return value.
	 *
	 * @param javadocReturn the javadoc return value
	 */
	public void setJavadocReturn(String javadocReturn) {
		this.javadocReturn = javadocReturn;
	}

	/**
	 * Gets locale.
	 *
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}
}
