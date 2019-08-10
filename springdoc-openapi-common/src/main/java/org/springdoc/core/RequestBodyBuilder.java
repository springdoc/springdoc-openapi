package org.springdoc.core;

import static org.springdoc.core.Constants.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.method.HandlerMethod;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;

@SuppressWarnings({ "rawtypes" })
@Component
public class RequestBodyBuilder {

	private ParameterBuilder parameterBuilder;

	private RequestBodyBuilder(ParameterBuilder parameterBuilder) {
		super();
		this.parameterBuilder = parameterBuilder;
	}

	public Optional<RequestBody> buildRequestBodyFromDoc(
			io.swagger.v3.oas.annotations.parameters.RequestBody requestBody,
			String[] classConsumes, String[] methodConsumes, Components components, JsonView jsonViewAnnotation) {
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

	public RequestBody calculateRequestBody(Components components, HandlerMethod handlerMethod, Operation operation,
			MediaAttributes mediaAttributes, String[] pNames, java.lang.reflect.Parameter[] parameters, int i,
			io.swagger.v3.oas.annotations.Parameter parameterDoc) {
		RequestBody requestBody = null;

		io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyDoc = parameterBuilder.getParameterAnnotation(
				handlerMethod,
				parameters[i], i, io.swagger.v3.oas.annotations.parameters.RequestBody.class);

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
				paramName);
	}

	protected RequestBody buildRequestBody(RequestBody requestBody, Components components, String[] allConsumes,
			java.lang.reflect.Parameter parameter, io.swagger.v3.oas.annotations.Parameter parameterDoc,
			String paramName) {
		if (requestBody == null)
			requestBody = new RequestBody();

		Schema<?> schema = parameterBuilder.calculateSchema(components, parameter, paramName);
		io.swagger.v3.oas.models.media.MediaType mediaType = null;
		if (schema != null && schema.getType() != null) {
			mediaType = new io.swagger.v3.oas.models.media.MediaType();
			mediaType.setSchema(schema);
		} else {
			Type returnType = parameter.getType();
			mediaType = calculateSchema(components, returnType);
		}

		Content content1 = new Content();
		if (ArrayUtils.isNotEmpty(allConsumes)) {
			for (String value : allConsumes) {
				setMediaTypeToContent(schema, content1, value);
			}
		} else {
			content1.addMediaType(MediaType.ALL_VALUE, mediaType);
		}
		requestBody.setContent(content1);
		if (parameterDoc != null) {
			if (StringUtils.isNotBlank(parameterDoc.description()))
				requestBody.setDescription(parameterDoc.description());
			requestBody.setRequired(parameterDoc.required());
		}
		return requestBody;
	}

	private io.swagger.v3.oas.models.media.MediaType calculateSchema(Components components, Type returnType) {
		ResolvedSchema resolvedSchema = ModelConverters.getInstance()
				.resolveAsResolvedSchema(new AnnotatedType(returnType).resolveAsRef(true));
		io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();
		if (resolvedSchema.schema != null) {
			Schema<?> returnTypeSchema = resolvedSchema.schema;
			if (returnTypeSchema != null) {
				mediaType.setSchema(returnTypeSchema);
			}
			Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				schemaMap.forEach(components::addSchemas);
			}
		}
		return mediaType;
	}

	private void setMediaTypeToContent(Schema schema, Content content, String value) {
		io.swagger.v3.oas.models.media.MediaType mediaTypeObject = new io.swagger.v3.oas.models.media.MediaType();
		mediaTypeObject.setSchema(schema);
		content.addMediaType(value, mediaTypeObject);
	}
}
