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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SuppressWarnings("rawtypes")
@Component
public class ResponseBuilder extends AbstractResponseBuilder {

	public Content buildContent(Components components, Method method, String[] methodProduces) {
		Content content = new Content();
		Schema schemaN = null;
		Type returnType = method.getGenericReturnType();
		if (returnType instanceof ParameterizedType) {
			schemaN = calculateSchemaFromParameterizedType(components, returnType);
		} else if (returnType instanceof TypeVariable) {
			schemaN = AnnotationsUtils.resolveSchemaFromType((Class) returnType, null, null);
		} else if (Void.TYPE.equals(returnType) || ResponseEntity.class.getName().equals(returnType.getTypeName())) {
			// if void, no content
			schemaN = AnnotationsUtils.resolveSchemaFromType(String.class, null, null);
		}
		if (schemaN == null) {
			schemaN = extractSchema(components, returnType);
		}

		if (schemaN == null && returnType instanceof Class) {
			schemaN = AnnotationsUtils.resolveSchemaFromType((Class) returnType, null, null);
		}

		if (schemaN != null) {
			io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();
			mediaType.setSchema(schemaN);
			// Fill the content
			setContent(methodProduces, content, mediaType);
		}
		return content;
	}

	private Schema calculateSchemaFromParameterizedType(Components components, Type returnType) {
		Schema schemaN = null;
		ParameterizedType parameterizedType = (ParameterizedType) returnType;
		if (Mono.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())
				|| Flux.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())) {
			if (parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType && ResponseEntity.class
					.getName().contentEquals(((ParameterizedType) parameterizedType.getActualTypeArguments()[0])
							.getRawType().getTypeName())) {
				ParameterizedType parameterizedTypeNew = (ParameterizedType) parameterizedType
						.getActualTypeArguments()[0];
				schemaN = calculateSchemaParameterizedType(components, parameterizedTypeNew);
			} else {
				schemaN = calculateSchemaParameterizedType(components, parameterizedType);
			}
		}
		return schemaN;
	}
}
