package org.springdoc.core;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ResponseBuilder extends AbstractResponseBuilder {

    public ResponseBuilder(OperationBuilder operationBuilder, List<ReturnTypeParser> returnTypeParsers) {
        super(operationBuilder, returnTypeParsers);
    }

    @Override
    protected Schema calculateSchemaFromParameterizedType(Components components, ParameterizedType parameterizedType,
                                                          JsonView jsonView) {
        Schema schemaN = null;
        if (Mono.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())) {
            if (parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType && isResponseTypeToIgnore(((ParameterizedType) parameterizedType.getActualTypeArguments()[0])
                    .getRawType().getTypeName())) {
                ParameterizedType parameterizedTypeNew = (ParameterizedType) parameterizedType
                        .getActualTypeArguments()[0];
                schemaN = calculateSchemaParameterizedType(components, parameterizedTypeNew, jsonView);
            } else {
                schemaN = calculateSchemaParameterizedType(components, parameterizedType, jsonView);
            }
        } else if (Flux.class.getName().contentEquals(parameterizedType.getRawType().getTypeName())) {
            if (parameterizedType.getActualTypeArguments()[0] instanceof ParameterizedType && isResponseTypeToIgnore(((ParameterizedType) parameterizedType.getActualTypeArguments()[0])
                    .getRawType().getTypeName())) {
                ParameterizedType parameterizedTypeNew = (ParameterizedType) parameterizedType
                        .getActualTypeArguments()[0];
                schemaN = calculateFluxSchema(components, parameterizedTypeNew, jsonView);
            } else {
                schemaN = calculateFluxSchema(components, parameterizedType, jsonView);
            }
        } else if (isResponseTypeToIgnore(parameterizedType.getRawType().getTypeName())) {
            schemaN = calculateSchemaParameterizedType(components, parameterizedType, jsonView);
        }
        return schemaN;
    }

    private boolean isResponseTypeToIgnore(String typeName) {
        return ResponseEntity.class.getName().contentEquals(typeName) || HttpEntity.class.getName().contentEquals(typeName);
    }

    private Schema calculateFluxSchema(Components components, ParameterizedType parameterizedType, JsonView jsonView) {
        Schema itemsSchema = SpringDocAnnotationsUtils.extractSchema(components, parameterizedType.getActualTypeArguments()[0], jsonView);
        return new ArraySchema().items(itemsSchema);
    }

}
