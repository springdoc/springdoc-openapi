package org.springdoc.subclass;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.AbstractParameterBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Class which sub class AbstractParameterBuilder in a different package and makes sure base class access is not changed. .
 */
public class ParameterBuilder extends AbstractParameterBuilder {

    @Override
    protected Schema calculateSchemaFromParameterizedType(Components components, Type returnType, JsonView jsonView) {
        return null;
    }

    @Override
    protected boolean isFile(ParameterizedType parameterizedType) {
        return false;
    }

    @Override
    protected boolean isFile(JavaType ct) {
        return false;
    }
}
