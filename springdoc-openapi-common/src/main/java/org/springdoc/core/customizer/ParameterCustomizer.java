package org.springdoc.core.customizer;

import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.web.method.HandlerMethod;

/**
 * Implement and register a bean of type {@link ParameterCustomizer} to customize a parameter
 * based on the parameter and handler method input
 */
public interface ParameterCustomizer {
    /**
     * @param parameterModel to be customized
     * @param parameter original parameter from handler method
     * @param handlerMethod handler method
     * @return customized parameter
     */
    Parameter customize(Parameter parameterModel, java.lang.reflect.Parameter parameter, HandlerMethod handlerMethod);
}
