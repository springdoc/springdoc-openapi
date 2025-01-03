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

import java.lang.reflect.Field;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * The type Parameter object naming strategy customizer.
 *
 * @author bnasslahsen
 */
public class ParameterObjectNamingStrategyCustomizer implements DelegatingMethodParameterCustomizer {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ParameterObjectNamingStrategyCustomizer.class);

	@Override
	public void customize(MethodParameter originalParameter, MethodParameter methodParameter) {
		if (AnnotatedElementUtils.isAnnotated(methodParameter.getContainingClass(), JsonNaming.class)) {
			JsonNaming jsonNaming = methodParameter.getContainingClass().getAnnotation(JsonNaming.class);
			if (jsonNaming.value().equals(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)) {
				try {
					Field parameterNameField = FieldUtils.getDeclaredField(methodParameter.getClass(), "parameterName",
							true);
					parameterNameField.set(methodParameter,
							PropertyNamingStrategies.UpperSnakeCaseStrategy.INSTANCE.translate(
									methodParameter.getParameterName()));
				}
				catch (IllegalAccessException e) {
					LOGGER.warn(e.getMessage());
				}
			}
		}
	}
}
