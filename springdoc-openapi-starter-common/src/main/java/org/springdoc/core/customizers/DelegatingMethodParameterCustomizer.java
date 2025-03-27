/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package org.springdoc.core.customizers;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * The interface Delegating method parameter customizer.
 * @author dyun
 */
@FunctionalInterface
public interface DelegatingMethodParameterCustomizer {
	/**
	 * Customize.
	 * tip: parameters include the parent fields, you can choose how to deal with the methodParameters
	 *
	 * @param originalParameter  the original parameter
	 * @param methodParameters the exploded parameters
     */
	@Nullable
	default void customizeList(MethodParameter originalParameter, List<MethodParameter> methodParameters) {
		methodParameters.forEach(parameter -> customize(originalParameter, parameter));
	}

	/**
	 * Customize.
	 *
	 * @param originalParameter the original parameter
	 * @param methodParameter   the method parameter
	 */
	void customize(MethodParameter originalParameter, MethodParameter methodParameter);

}
