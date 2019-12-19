package org.springdoc.core;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.ParameterizedType;

@SuppressWarnings("rawtypes")
public class ResponseBuilder extends AbstractResponseBuilder {

    public ResponseBuilder(OperationBuilder operationBuilder) {
        super(operationBuilder);
    }

    @Override
    protected Schema calculateSchemaFromParameterizedType(Components components, ParameterizedType parameterizedType,
                                                          JsonView jsonView) {
        Schema<?> schemaN = null;
        if (ResponseEntity.class.getName().contentEquals(parameterizedType.getRawType().getTypeName()) || HttpEntity.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())) {
            schemaN = calculateSchemaParameterizedType(components, parameterizedType, jsonView);
        }
        return schemaN;
    }

}
