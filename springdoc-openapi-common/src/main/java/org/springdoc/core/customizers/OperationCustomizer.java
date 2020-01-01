package org.springdoc.core.customizers;

import io.swagger.v3.oas.models.Operation;
import org.springframework.web.method.HandlerMethod;

/**
 * Implement and register a bean of type {@link OperationCustomizer} to customize an operation
 * based on the handler method input
 */
public interface OperationCustomizer {

    /**
     * @param operation     input operation
     * @param handlerMethod original handler method
     * @return customized operation
     */
    Operation customize(Operation operation, HandlerMethod handlerMethod);
}

