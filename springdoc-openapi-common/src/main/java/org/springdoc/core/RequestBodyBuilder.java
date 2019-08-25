package org.springdoc.core;

import static org.springdoc.core.Constants.*;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.method.HandlerMethod;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;

@SuppressWarnings("rawtypes")
@Component
public class RequestBodyBuilder {

	private ParameterBuilder parameterBuilder;

	public RequestBodyBuilder(ParameterBuilder parameterBuilder) {
		super();
		this.parameterBuilder = parameterBuilder;
	}

	public Optional<RequestBody> buildRequestBodyFromDoc(
			io.swagger.v3.oas.annotations.parameters.RequestBody requestBody, String[] classConsumes,
			String[] methodConsumes, Components components, JsonView jsonViewAnnotation) {
		if (requestBody == null) {
			return Optional.empty();
		}
		RequestBody requestBodyObject = new RequestBody();
		boolean isEmpty = true;

		if (StringUtils.isNotBlank(requestBody.ref())) {
			requestBodyObject.set$ref(requestBody.ref());
			return Optional.of(requestBodyObject);
		}

		if (StringUtils.isNotBlank(requestBody.description())) {
			requestBodyObject.setDescription(requestBody.description());
			isEmpty = false;
		} else {
			requestBodyObject.setDescription(DEFAULT_DESCRIPTION);
		}
		if (requestBody.required()) {
			requestBodyObject.setRequired(requestBody.required());
			isEmpty = false;
		}
		if (requestBody.extensions().length > 0) {
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(requestBody.extensions());
			if (extensions != null) {
				for (Map.Entry<String, Object> entry : extensions.entrySet()) {
					requestBodyObject.addExtension(entry.getKey(), entry.getValue());
				}
			}
			isEmpty = false;
		}

		if (requestBody.content().length > 0) {
			isEmpty = false;
		}

		if (isEmpty) {
			return Optional.empty();
		}
		AnnotationsUtils
				.getContent(requestBody.content(), classConsumes == null ? new String[0] : classConsumes,
						methodConsumes == null ? new String[0] : methodConsumes, null, components, jsonViewAnnotation)
				.ifPresent(requestBodyObject::setContent);
		return Optional.of(requestBodyObject);
	}

	public RequestBody calculateRequestBody(Components components, HandlerMethod handlerMethod,
			MediaAttributes mediaAttributes, String[] pNames, java.lang.reflect.Parameter[] parameters, int i,
			io.swagger.v3.oas.annotations.Parameter parameterDoc, Schema mergedSchema) {
		RequestBody requestBody = null;

		io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyDoc = parameterBuilder.getParameterAnnotation(
				handlerMethod, parameters[i], i, io.swagger.v3.oas.annotations.parameters.RequestBody.class);

		// use documentation as reference
		if (requestBodyDoc != null) {
			requestBody = buildRequestBodyFromDoc(requestBodyDoc, mediaAttributes.getClassConsumes(),
					mediaAttributes.getMethodConsumes(), components, null).orElse(null);
		}

		RequestPart requestPart = parameterBuilder.getParameterAnnotation(handlerMethod, parameters[i], i,
				RequestPart.class);
		String paramName = null;
		if (requestPart != null)
			paramName = StringUtils.defaultIfEmpty(requestPart.value(), requestPart.name());
		paramName = StringUtils.defaultIfEmpty(paramName, pNames[i]);
		return buildRequestBody(requestBody, components, mediaAttributes.getAllConsumes(), parameters[i], parameterDoc,
				paramName, mergedSchema);
	}

	protected RequestBody buildRequestBody(RequestBody requestBody, Components components, String[] allConsumes,
			java.lang.reflect.Parameter parameter, io.swagger.v3.oas.annotations.Parameter parameterDoc,
			String paramName, Schema mergedSchema) {
		if (requestBody == null)
			requestBody = new RequestBody();

		Schema<?> schema = parameterBuilder.calculateSchema(components, parameter, paramName, null, mergedSchema);

		Content content = new Content();

		for (String value : allConsumes) {
			setMediaTypeToContent(schema, content, value);
		}

		requestBody.setContent(content);
		if (parameterDoc != null) {
			if (StringUtils.isNotBlank(parameterDoc.description()))
				requestBody.setDescription(parameterDoc.description());
			requestBody.setRequired(parameterDoc.required());
		}
		return requestBody;
	}

	private void setMediaTypeToContent(Schema schema, Content content, String value) {
		io.swagger.v3.oas.models.media.MediaType mediaTypeObject = new io.swagger.v3.oas.models.media.MediaType();
		mediaTypeObject.setSchema(schema);
		content.addMediaType(value, mediaTypeObject);
	}
}
