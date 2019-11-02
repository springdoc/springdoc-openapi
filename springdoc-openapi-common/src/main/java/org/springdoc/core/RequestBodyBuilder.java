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

	private AbstractParameterBuilder parameterBuilder;

	public RequestBodyBuilder(AbstractParameterBuilder parameterBuilder) {
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

	public void calculateRequestBodyInfo(Components components, HandlerMethod handlerMethod,
			MethodAttributes methodAttributes, int i, ParameterInfo parameterInfo, RequestBodyInfo requestBodyInfo) {
		RequestBody requestBody = requestBodyInfo.getRequestBody();

		// Get it from parameter level, if not present
		if (requestBody == null) {
			io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyDoc = parameterBuilder
					.getParameterAnnotation(handlerMethod, parameterInfo.getParameter(), i,
							io.swagger.v3.oas.annotations.parameters.RequestBody.class);
			requestBody = this.buildRequestBodyFromDoc(requestBodyDoc, methodAttributes.getClassConsumes(),
					methodAttributes.getMethodConsumes(), components, null).orElse(null);
		}

		RequestPart requestPart = parameterBuilder.getParameterAnnotation(handlerMethod, parameterInfo.getParameter(),
				i, RequestPart.class);
		String paramName = null;
		if (requestPart != null)
			paramName = StringUtils.defaultIfEmpty(requestPart.value(), requestPart.name());
		paramName = StringUtils.defaultIfEmpty(paramName, parameterInfo.getpName());
		parameterInfo.setpName(paramName);

		requestBody = buildRequestBody(requestBody, components, methodAttributes, parameterInfo,
				requestBodyInfo);
		requestBodyInfo.setRequestBody(requestBody);
	}

	private RequestBody buildRequestBody(RequestBody requestBody, Components components,
			MethodAttributes methodAttributes,
			ParameterInfo parameterInfo, RequestBodyInfo requestBodyInfo) {
		if (requestBody == null)
			requestBody = new RequestBody();

		if (requestBody.getContent() == null
				|| (requestBody.getContent() != null && methodAttributes.isMethodOverloaded())) {

			Schema<?> schema = parameterBuilder.calculateSchema(components, parameterInfo.getParameter(),
					parameterInfo.getpName(), null, requestBodyInfo,
					methodAttributes.getJsonViewAnnotationForRequestBody());
			Content content = requestBody.getContent();
			content = buildContent(methodAttributes, schema, content);
			requestBody.setContent(content);
		}

		if (parameterInfo.getParameterDoc() != null) {
			io.swagger.v3.oas.annotations.Parameter parameterDoc = parameterInfo.getParameterDoc();
			if (StringUtils.isNotBlank(parameterDoc.description()))
				requestBody.setDescription(parameterDoc.description());
			requestBody.setRequired(parameterDoc.required());
		}

		return requestBody;
	}

	private Content buildContent(MethodAttributes methodAttributes, Schema<?> schema, Content content) {
		if (methodAttributes.isMethodOverloaded() && content != null) {
			for (String value : methodAttributes.getAllConsumes()) {
				setMediaTypeToContent(schema, content, value);
			}
		} else {
			content = new Content();
			for (String value : methodAttributes.getAllConsumes()) {
				setMediaTypeToContent(schema, content, value);
			}
		}
		return content;
	}

	private void setMediaTypeToContent(Schema schema, Content content, String value) {
		io.swagger.v3.oas.models.media.MediaType mediaTypeObject = new io.swagger.v3.oas.models.media.MediaType();
		mediaTypeObject.setSchema(schema);
		content.addMediaType(value, mediaTypeObject);
	}
}
