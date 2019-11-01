package org.springdoc.core;

import java.lang.reflect.ParameterizedType;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;

@SuppressWarnings("rawtypes")
@Component
public class ResponseBuilder extends AbstractResponseBuilder {

	public ResponseBuilder(OperationBuilder operationBuilder) {
		super(operationBuilder);
	}

	@Override
	protected Schema calculateSchemaFromParameterizedType(Components components, ParameterizedType parameterizedType) {
		Schema<?> schemaN = null;
		if (ResponseEntity.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())) {
			schemaN = calculateSchemaParameterizedType(components, parameterizedType);
		}
		return schemaN;
	}

}
