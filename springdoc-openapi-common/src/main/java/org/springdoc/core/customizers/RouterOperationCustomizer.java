package org.springdoc.core.customizers;

import org.springdoc.core.fn.RouterOperation;

import org.springframework.web.method.HandlerMethod;

/**
 * Implement and register a bean of type {@link RouterOperationCustomizer} to customize an router operation
 * based on the handler method input on default OpenAPI descriptions but not groups
 *
 * @author hyeonisism
 */
@FunctionalInterface
public interface RouterOperationCustomizer {

	/**
	 * Customize router operation.
	 *
	 * @param routerOperation input operation
	 * @param handlerMethod original handler method
	 * @return customized router operation
	 */
	RouterOperation customize(RouterOperation routerOperation, HandlerMethod handlerMethod);

}
