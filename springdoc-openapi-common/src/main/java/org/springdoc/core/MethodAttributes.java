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

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.ArrayUtils;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class MethodAttributes {

	private String[] classProduces;

	private String[] classConsumes;

	private String[] methodProduces = {};

	private String[] methodConsumes = {};

	private boolean methodOverloaded;

	private boolean withApiResponseDoc;

	private JsonView jsonViewAnnotation;

	private JsonView jsonViewAnnotationForRequestBody;

	public MethodAttributes(String[] methodProducesNew) {
		this.methodProduces = methodProducesNew;
	}

	public MethodAttributes() {
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
			fillMethods(reqPostMappingMethod.produces(), reqPostMappingMethod.consumes());
			return;
		}
		GetMapping reqGetMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, GetMapping.class);
		if (reqGetMappingMethod != null) {
			fillMethods(reqGetMappingMethod.produces(), reqGetMappingMethod.consumes());
			return;
		}
		DeleteMapping reqDeleteMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, DeleteMapping.class);
		if (reqDeleteMappingMethod != null) {
			fillMethods(reqDeleteMappingMethod.produces(), reqDeleteMappingMethod.consumes());
			return;
		}
		PutMapping reqPutMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, PutMapping.class);
		if (reqPutMappingMethod != null) {
			fillMethods(reqPutMappingMethod.produces(), reqPutMappingMethod.consumes());
			return;
		}
		RequestMapping reqMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
		RequestMapping reqMappingClass = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), RequestMapping.class);

		if (reqMappingMethod != null && reqMappingClass != null) {
			fillMethods(ArrayUtils.addAll(reqMappingMethod.produces(), reqMappingClass.produces()), ArrayUtils.addAll(reqMappingMethod.consumes(), reqMappingClass.consumes()));
		}
		else if (reqMappingMethod != null) {
			fillMethods(reqMappingMethod.produces(), reqMappingMethod.consumes());
		}
		else if (reqMappingClass != null) {
			fillMethods(reqMappingClass.produces(), reqMappingClass.consumes());
		}
	}

	private void fillMethods(String[] produces, String[] consumes) {
		if (ArrayUtils.isNotEmpty(produces))
			methodProduces = produces;
		else if (ArrayUtils.isNotEmpty(classProduces))
			methodProduces = classProduces;
		else
			methodProduces = new String[] { MediaType.ALL_VALUE };

		if (ArrayUtils.isNotEmpty(consumes))
			methodConsumes = consumes;
		else if (ArrayUtils.isNotEmpty(classConsumes))
			methodConsumes = classConsumes;
		else
			methodConsumes = new String[] { MediaType.APPLICATION_JSON_VALUE };

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
}
