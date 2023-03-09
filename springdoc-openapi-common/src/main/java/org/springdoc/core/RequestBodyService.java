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

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.apache.commons.lang3.StringUtils;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestPart;

import static org.springdoc.core.SpringDocAnnotationsUtils.mergeSchema;


/**
 * The type Request body builder.
 * @author bnasslahsen
 */
public class RequestBodyService {

	/**
	 * The Parameter builder.
	 */
	private final GenericParameterService parameterBuilder;

	/**
	 * Instantiates a new Request body builder.
	 *
	 * @param parameterBuilder the parameter builder
	 */
	public RequestBodyService(GenericParameterService parameterBuilder) {
		super();
		this.parameterBuilder = parameterBuilder;
	}

	/**
	 * Build request body from doc optional.
	 *
	 * @param requestBody the request body
	 * @param requestBodyOp the request body op
	 * @param methodAttributes the method attributes
	 * @param components the components
	 * @param jsonViewAnnotation the json view annotation
	 * @return the optional
	 */
	public Optional<RequestBody> buildRequestBodyFromDoc(
			io.swagger.v3.oas.annotations.parameters.RequestBody requestBody, RequestBody requestBodyOp, MethodAttributes methodAttributes,
			Components components, JsonView jsonViewAnnotation) {
		String[] classConsumes = methodAttributes.getClassConsumes();
		String[] methodConsumes = methodAttributes.getMethodConsumes();

		if (requestBody == null)
			return Optional.empty();
		RequestBody requestBodyObject = new RequestBody();
		boolean isEmpty = true;

		if (StringUtils.isNotBlank(requestBody.ref())) {
			requestBodyObject.set$ref(requestBody.ref());
			return Optional.of(requestBodyObject);
		}

		if (StringUtils.isNotBlank(requestBody.description())) {
			requestBodyObject.setDescription(requestBody.description());
			isEmpty = false;
		}

		if (requestBody.required()) {
			requestBodyObject.setRequired(requestBody.required());
			isEmpty = false;
		}
		if (requestBody.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(requestBody.extensions());
			extensions.forEach(requestBodyObject::addExtension);
			isEmpty = false;
		}

		if (requestBody.content().length > 0)
			isEmpty = false;

		if (isEmpty)
			return Optional.empty();

		buildResquestBodyContent(requestBody, requestBodyOp, methodAttributes, components, jsonViewAnnotation, classConsumes, methodConsumes, requestBodyObject);

		return Optional.of(requestBodyObject);
	}

	/**
	 * Build resquest body content.
	 *
	 * @param requestBody the request body
	 * @param requestBodyOp the request body op
	 * @param methodAttributes the method attributes
	 * @param components the components
	 * @param jsonViewAnnotation the json view annotation
	 * @param classConsumes the class consumes
	 * @param methodConsumes the method consumes
	 * @param requestBodyObject the request body object
	 */
	private void buildResquestBodyContent(io.swagger.v3.oas.annotations.parameters.RequestBody requestBody, RequestBody requestBodyOp, MethodAttributes methodAttributes, Components components, JsonView jsonViewAnnotation, String[] classConsumes, String[] methodConsumes, RequestBody requestBodyObject) {
		Optional<Content> optionalContent = SpringDocAnnotationsUtils
				.getContent(requestBody.content(), getConsumes(classConsumes),
						getConsumes(methodConsumes), null, components, jsonViewAnnotation);
		if (requestBodyOp == null) {
			if (optionalContent.isPresent()) {
				Content content = optionalContent.get();
				requestBodyObject.setContent(content);
				if (containsResponseBodySchema(content))
					methodAttributes.setWithResponseBodySchemaDoc(true);
			}
		}
		else {
			Content existingContent = requestBodyOp.getContent();
			if (optionalContent.isPresent() && existingContent != null) {
				Content newContent = optionalContent.get();
				if (methodAttributes.isMethodOverloaded()) {
					Arrays.stream(methodAttributes.getMethodProduces()).filter(mediaTypeStr -> (newContent.get(mediaTypeStr) != null)).forEach(mediaTypeStr -> {
						if (newContent.get(mediaTypeStr).getSchema() != null)
							mergeSchema(existingContent, newContent.get(mediaTypeStr).getSchema(), mediaTypeStr);
					});
					requestBodyObject.content(existingContent);
				}
				else
					requestBodyObject.content(newContent);
			}
		}
	}

	/**
	 * Contains response body schema boolean.
	 *
	 * @param content the content
	 * @return the boolean
	 */
	private boolean containsResponseBodySchema(Content content) {
		return content.entrySet().stream().anyMatch(stringMediaTypeEntry -> stringMediaTypeEntry.getValue().getSchema() != null);
	}

	/**
	 * Get consumes string [ ].
	 *
	 * @param classConsumes the class consumes
	 * @return the string [ ]
	 */
	private String[] getConsumes(String[] classConsumes) {
		return classConsumes == null ? new String[0] : classConsumes;
	}

	/**
	 * Build request body from doc optional.
	 *
	 * @param requestBody the request body
	 * @param methodAttributes the method attributes
	 * @param components the components
	 * @return the optional
	 */
	public Optional<RequestBody> buildRequestBodyFromDoc(io.swagger.v3.oas.annotations.parameters.RequestBody requestBody,
			MethodAttributes methodAttributes, Components components) {
		return this.buildRequestBodyFromDoc(requestBody, null, methodAttributes,
				components, null);
	}

	/**
	 * Build request body from doc optional.
	 *
	 * @param requestBody the request body
	 * @param methodAttributes the method attributes
	 * @param components the components
	 * @param jsonViewAnnotation the json view annotation
	 * @return the optional
	 */
	public Optional<RequestBody> buildRequestBodyFromDoc(io.swagger.v3.oas.annotations.parameters.RequestBody requestBody,
			MethodAttributes methodAttributes, Components components, JsonView jsonViewAnnotation) {
		return this.buildRequestBodyFromDoc(requestBody, null, methodAttributes,
				components, jsonViewAnnotation);
	}

	/**
	 * Build request body from doc optional.
	 *
	 * @param requestBody the request body
	 * @param requestBodyOp the request body op
	 * @param methodAttributes the method attributes
	 * @param components the components
	 * @return the optional
	 */
	public Optional<RequestBody> buildRequestBodyFromDoc(
			io.swagger.v3.oas.annotations.parameters.RequestBody requestBody, RequestBody requestBodyOp, MethodAttributes methodAttributes,
			Components components) {
		return this.buildRequestBodyFromDoc(requestBody, requestBodyOp, methodAttributes,
				components, null);
	}

	/**
	 * Calculate request body info.
	 *
	 * @param components the components
	 * @param methodAttributes the method attributes
	 * @param parameterInfo the parameter info
	 * @param requestBodyInfo the request body info
	 */
	public void calculateRequestBodyInfo(Components components, MethodAttributes methodAttributes,
			ParameterInfo parameterInfo, RequestBodyInfo requestBodyInfo) {
		RequestBody requestBody = requestBodyInfo.getRequestBody();
		MethodParameter methodParameter = parameterInfo.getMethodParameter();
		// Get it from parameter level, if not present
		if (requestBody == null) {
			io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyDoc = methodParameter.getParameterAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody.class);
			requestBody = this.buildRequestBodyFromDoc(requestBodyDoc, methodAttributes, components).orElse(null);
		}

		RequestPart requestPart = methodParameter.getParameterAnnotation(RequestPart.class);
		String paramName = null;
		if (requestPart != null) {
			paramName = StringUtils.defaultIfEmpty(requestPart.value(), requestPart.name());
			parameterInfo.setRequired(requestPart.required());
			parameterInfo.setRequestPart(true);
		}
		paramName = StringUtils.defaultIfEmpty(paramName, parameterInfo.getpName());
		parameterInfo.setpName(paramName);

		requestBody = buildRequestBody(requestBody, components, methodAttributes, parameterInfo,
				requestBodyInfo);
		requestBodyInfo.setRequestBody(requestBody);
	}

	/**
	 * Build request body request body.
	 *
	 * @param requestBody the request body
	 * @param components the components
	 * @param methodAttributes the method attributes
	 * @param parameterInfo the parameter info
	 * @param requestBodyInfo the request body info
	 * @return the request body
	 */
	private RequestBody buildRequestBody(RequestBody requestBody, Components components,
			MethodAttributes methodAttributes,
			ParameterInfo parameterInfo, RequestBodyInfo requestBodyInfo) {
		if (requestBody == null) {
			requestBody = new RequestBody();
			requestBodyInfo.setRequestBody(requestBody);
		}

		if (requestBody.getContent() == null) {
			Schema<?> schema = parameterBuilder.calculateSchema(components, parameterInfo, requestBodyInfo,
					methodAttributes.getJsonViewAnnotationForRequestBody());
			buildContent(requestBody, methodAttributes, schema);
		}
		else if (!methodAttributes.isWithResponseBodySchemaDoc()) {
			Schema<?> schema = parameterBuilder.calculateSchema(components, parameterInfo, requestBodyInfo,
					methodAttributes.getJsonViewAnnotationForRequestBody());
			mergeContent(requestBody, methodAttributes, schema);
		}

		// Add requestBody javadoc
		if (StringUtils.isBlank(requestBody.getDescription()) && parameterBuilder.getJavadocProvider() != null
				&& parameterBuilder.isRequestBodyPresent(parameterInfo)) {
			String paramJavadocDescription = parameterBuilder.getParamJavadoc(parameterBuilder.getJavadocProvider(), parameterInfo.getMethodParameter());
			if (!StringUtils.isBlank(paramJavadocDescription)) {
				requestBody.setDescription(paramJavadocDescription);
			}
		}
		return requestBody;
	}

	/**
	 * Merge content.
	 *
	 * @param requestBody the request body
	 * @param methodAttributes the method attributes
	 * @param schema the schema
	 */
	private void mergeContent(RequestBody requestBody, MethodAttributes methodAttributes, Schema<?> schema) {
		Content content = requestBody.getContent();
		buildContent(requestBody, methodAttributes, schema, content);
	}

	/**
	 * Build content.
	 *
	 * @param requestBody the request body
	 * @param methodAttributes the method attributes
	 * @param schema the schema
	 */
	private void buildContent(RequestBody requestBody, MethodAttributes methodAttributes, Schema<?> schema) {
		Content content = new Content();
		buildContent(requestBody, methodAttributes, schema, content);
	}

	/**
	 * Build content.
	 *
	 * @param requestBody the request body
	 * @param methodAttributes the method attributes
	 * @param schema the schema
	 * @param content the content
	 */
	private void buildContent(RequestBody requestBody, MethodAttributes methodAttributes, Schema<?> schema, Content content) {
		for (String value : methodAttributes.getMethodConsumes()) {
			io.swagger.v3.oas.models.media.MediaType mediaTypeObject = new io.swagger.v3.oas.models.media.MediaType();
			mediaTypeObject.setSchema(schema);
			MediaType mediaType = content.get(value);
			if (mediaType != null) {
				if (mediaType.getExample() != null)
					mediaTypeObject.setExample(mediaType.getExample());
				if (mediaType.getExamples() != null)
					mediaTypeObject.setExamples(mediaType.getExamples());
				if (mediaType.getEncoding() != null)
					mediaTypeObject.setEncoding(mediaType.getEncoding());
			}
			content.addMediaType(value, mediaTypeObject);
		}
		requestBody.setContent(content);
	}
}
