package org.springdoc.core;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Class which sub class AbstractParameterBuilder in a different package and makes sure base class access is not changed. .
 */
public class ParameterBuilder extends AbstractParameterBuilder {

    public ParameterBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
        super(localSpringDocParameterNameDiscoverer);
    }

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
