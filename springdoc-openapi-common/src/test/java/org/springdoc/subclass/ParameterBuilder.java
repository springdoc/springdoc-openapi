package org.springdoc.subclass;

import com.fasterxml.jackson.databind.JavaType;
import org.springdoc.core.AbstractParameterBuilder;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.ParameterizedType;

/**
 * Class which sub class AbstractParameterBuilder in a different package and makes sure base class access is not changed. .
 */
public class ParameterBuilder extends AbstractParameterBuilder {

    public ParameterBuilder(LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer) {
        super(localSpringDocParameterNameDiscoverer);
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
