/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app179;

import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import org.springdoc.core.customizers.ParameterCustomizer;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

@Component
public class MyPathParameterCustomizer implements ParameterCustomizer {
	@Override
	public Parameter customize(Parameter parameterModel, MethodParameter methodParameter) {
		if (methodParameter.hasParameterAnnotation(MyIdPathVariable.class)) {
			Parameter alternativeParameter = new PathParameter();
			alternativeParameter.setName("objId");
			alternativeParameter.setSchema(new StringSchema());
			return alternativeParameter;
		}
		return parameterModel;
	}
}