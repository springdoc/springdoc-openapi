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

package test.org.springdoc.api.v30.app92;

import io.swagger.v3.oas.models.parameters.Parameter;
import jakarta.validation.constraints.NotNull;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

/**
 * The type Parameter customizer.
 */
@Component
class ParameterCustomizer implements org.springdoc.core.customizers.ParameterCustomizer {
	/**
	 * Customize parameter.
	 *
	 * @param parameterModel  the parameter model
	 * @param methodParameter the method parameter
	 * @return the parameter
	 */
	@Override
	public Parameter customize(Parameter parameterModel, MethodParameter methodParameter) {
		NotNull annotation = methodParameter.getParameterAnnotation(NotNull.class);
		if (annotation != null) {
			parameterModel.required(false);
		}
		return parameterModel;
	}
}
