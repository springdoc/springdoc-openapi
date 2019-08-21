package org.springdoc.core;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;

@Component
public class ResponseBuilder extends AbstractResponseBuilder {

	private ResponseBuilder(OperationBuilder operationBuilder) {
		super(operationBuilder);
	}

	public Content buildContent(Components components, Method method, String[] methodProduces) {
		Content content = new Content();
		Schema<?> schemaN = null;
		Type returnType = method.getGenericReturnType();
		if (returnType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) returnType;
			if (ResponseEntity.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())) {
				schemaN = calculateSchemaParameterizedType(components, parameterizedType);
			}
		} else if (returnType instanceof TypeVariable) {
			schemaN = AnnotationsUtils.resolveSchemaFromType((Class<?>) returnType, null, null);
		} else if (ResponseEntity.class.getName().equals(returnType.getTypeName())) {
			schemaN = AnnotationsUtils.resolveSchemaFromType(String.class, null, null);
		}

		if (Void.TYPE.equals(returnType)) {
			// if void, no content
			content = null;
		} else {
			if (schemaN == null) {
				schemaN = extractSchema(components, returnType);
			}
			if (schemaN == null && returnType instanceof Class) {
				schemaN = AnnotationsUtils.resolveSchemaFromType((Class<?>) returnType, null, null);
			}

			if (schemaN != null) {
				io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();
				mediaType.setSchema(schemaN);
				// Fill the content
				setContent(methodProduces, content, mediaType);
			}
		}
		return content;
	}

}
