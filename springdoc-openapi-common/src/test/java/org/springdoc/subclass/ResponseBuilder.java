package org.springdoc.subclass;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.AbstractResponseBuilder;
import org.springdoc.core.OperationBuilder;

import java.lang.reflect.ParameterizedType;

/**
 * Class which sub class AbstractResponseBuilder in a different package and makes sure base class access is not changed. .
 */
public class ResponseBuilder extends AbstractResponseBuilder {

    public ResponseBuilder(OperationBuilder operationBuilder) {
        super(operationBuilder);
    }

    @Override
    protected Schema calculateSchemaFromParameterizedType(Components components, ParameterizedType returnType, JsonView jsonView) {
        return null;
    }


}
