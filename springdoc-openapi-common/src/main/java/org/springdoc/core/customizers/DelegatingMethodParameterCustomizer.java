package org.springdoc.core.customizers;

import org.springframework.core.MethodParameter;

/**
 * The interface Delegating method parameter customizer.
 */
@FunctionalInterface
public interface DelegatingMethodParameterCustomizer {

	/**
	 * Customize.
	 *
	 * @param originalParameter the original parameter 
	 * @param methodParameter the method parameter
	 */
	void customize(MethodParameter originalParameter, MethodParameter methodParameter);

}
